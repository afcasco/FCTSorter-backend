package dev.afcasco.fctsorterbackend.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String newPath;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, String newPath) {
        super(request);
        this.newPath = newPath;
    }

    @Override
    public String getRequestURI() {
        return newPath;
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        url.append(getScheme()).append("://").append(getServerName()).append(":").append(getServerPort())
                .append(newPath);
        return url;
    }
}
