package dev.afcasco.fctsorterbackend.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Filter to remove trailing slashes from the requested URIs.
 * There might be a better way using tomcat configuration or something else that does not
 * need to look for / alter every request.
 */
public class TrailingSlashRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();

        if (hasTrailingSlash(path)) {
            filterChain.doFilter(requestWithoutTrailingSlash(request), servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean hasTrailingSlash(String path) {
        return path.endsWith("/");
    }

    private HttpServletRequest requestWithoutTrailingSlash(HttpServletRequest request) {
        String path = request.getRequestURI();
        String newPath = path.substring(0, path.length() - 1);
        return new CustomHttpServletRequestWrapper(request, newPath);
    }
}