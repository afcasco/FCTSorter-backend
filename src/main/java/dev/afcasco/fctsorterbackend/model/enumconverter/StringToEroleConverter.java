package dev.afcasco.fctsorterbackend.model.enumconverter;

import dev.afcasco.fctsorterbackend.exception.RoleNotFoundException;
import dev.afcasco.fctsorterbackend.model.ERole;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEroleConverter implements Converter<String, ERole>{
    @SneakyThrows
    @Override
    public ERole convert(String source) {
        return switch (source) {
            case "admin" -> ERole.ROLE_ADMIN;
            case "mod" -> ERole.ROLE_MODERATOR;
            case "user" -> ERole.ROLE_USER;
            default -> throw new RoleNotFoundException(source);
        };
    }
}
