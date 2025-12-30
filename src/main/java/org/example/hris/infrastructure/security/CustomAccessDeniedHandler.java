package org.example.hris.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Class ini akan menangani error jika user yang sudah login (terautentikasi)
 * mencoba mengakses endpoint yang tidak sesuai dengan rolenya (Error 403 Forbidden).
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Set status ke 403
        response.getWriter().write("{ \"status\": 403, \"error\": \"Forbidden\", \"message\": \"Anda tidak memiliki hak akses untuk sumber daya ini.\" }");
    }
}