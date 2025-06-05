package com.example.desafio_vendas.controller;

import com.example.desafio_vendas.dto.TokenJWTDTO;
import com.example.desafio_vendas.dto.UsuarioLoginDTO;
import com.example.desafio_vendas.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtTokenService jwtTokenService;

    // Usuário e senha estáticos (defina em application.properties ou hardcoded para simplicidade)
    @Value("${api.static.user.username}")
    private String staticUsername;

    @Value("${api.static.user.password}")
    private String staticPassword;


    @PostMapping("/login")
    public ResponseEntity<TokenJWTDTO> login(@RequestBody @Valid UsuarioLoginDTO dadosLogin) {
        // Validação do usuário estático
        // Em um cenário real, você usaria o AuthenticationManager do Spring Security
        // e um UserDetailsService para validar contra um banco de dados.
        if (staticUsername.equals(dadosLogin.getUsuario()) && staticPassword.equals(dadosLogin.getSenha())) {
            String tokenJWT = jwtTokenService.gerarToken(dadosLogin.getUsuario());
            return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
        } else {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }
    }
}
