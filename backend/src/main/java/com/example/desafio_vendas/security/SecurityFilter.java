package com.example.desafio_vendas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    // Para o usuário estático, não precisamos de um UserDetailsService complexo agora.
    // Em um cenário real, você usaria um UserDetailsService para carregar dados do usuário do banco.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            String subject = jwtTokenService.validarToken(tokenJWT);
            if (subject != null) {
                // Para o usuário estático "admin"
                // Em um app real, você carregaria o UserDetails do banco aqui
                UserDetails userDetails = User.withUsername(subject)
                                            .password("") // A senha não é necessária aqui, pois o token já foi validado
                                            .authorities(Collections.emptyList()) // Adicione roles/authorities se necessário
                                            .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
