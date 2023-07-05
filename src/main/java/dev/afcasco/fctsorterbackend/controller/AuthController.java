package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.exception.AlreadyLoggedInException;
import dev.afcasco.fctsorterbackend.model.RefreshToken;
import dev.afcasco.fctsorterbackend.exception.TokenRefreshException;
import dev.afcasco.fctsorterbackend.repository.RoleRepository;
import dev.afcasco.fctsorterbackend.repository.UserRepository;
import dev.afcasco.fctsorterbackend.model.ERole;
import dev.afcasco.fctsorterbackend.model.Role;
import dev.afcasco.fctsorterbackend.model.User;
import dev.afcasco.fctsorterbackend.payload.request.LoginRequest;
import dev.afcasco.fctsorterbackend.payload.request.SignupRequest;
import dev.afcasco.fctsorterbackend.payload.response.MessageResponse;
import dev.afcasco.fctsorterbackend.payload.response.UserInfoResponse;
import dev.afcasco.fctsorterbackend.security.jwt.JwtUtils;
import dev.afcasco.fctsorterbackend.security.services.UserDetailsImpl;
import dev.afcasco.fctsorterbackend.security.services.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Auth", description = "Authentication Management Endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthenticationManager authenticationManager, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws AlreadyLoggedInException {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // TODO see if this fixes the random login failures because there's already a refresh token in the db
        //refreshTokenService.deleteByUserId(userDetails.getId());

        // createRefreshToken throws a sqlconstraintviolation if user was already
        // signed in, trying to save the refresh token (which is a one to one relationship)
        // this captures the exception and returns an ErrorMessage
        RefreshToken refreshToken;
        try {
            refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        } catch (Exception e) {
            throw new AlreadyLoggedInException(loginRequest.getUsername());
            //return ResponseEntity.ok().body(new MessageResponse("User already logged in"));

        }

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
                    new RuntimeException("Error: Role is not found."));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() ->
                                new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() ->
                                new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
                                new RuntimeException("Error:  Role is not found"));
                        roles.add(userRole);
                    }
                }
            });

            user.setRoles(roles);
            userRepository.save(user);
        }

        return ResponseEntity.ok(new MessageResponse("User register successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!principle.toString().equals("anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You have been signed out!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && refreshToken.length() > 0) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token refreshed successfully!"));
                    }).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found in the database"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is empty!"));
    }


}