package com.tensai.cms.auth.internal.config;

import com.tensai.cms.auth.internal.entity.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramAuthFilter extends OncePerRequestFilter {
    private final TelegramSecurityProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String secret = request.getHeader(properties.header());

        if (secret == null || secret.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (properties.secret().equals(secret)) {
            var auth = new UsernamePasswordAuthenticationToken(
                    "TelegramGateway",
                    null,
                    List.of(new SimpleGrantedAuthority(UserRole.TELEGRAM.getAuthority()))
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }
}
