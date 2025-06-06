package com.example.desafio_vendas.controller;

import com.example.desafio_vendas.dto.TokenJWTDTO;
import com.example.desafio_vendas.dto.UsuarioLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoint para obter o token de autenticação.")
public class AuthenticationController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Value("${api.static.user.username}")
    private String staticUsername;

    @Value("${api.static.user.password}")
    private String staticPassword;

    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuário",
               description = "Autentica um usuário com base em credenciais estáticas e retorna um token JWT.")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido, retorna o token JWT.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenJWTDTO.class)))
    @ApiResponse(responseCode = "400", description = "Credenciais inválidas.")
    public ResponseEntity<TokenJWTDTO> login(@RequestBody UsuarioLoginDTO dadosLogin) {
        if (staticUsername.equals(dadosLogin.getUsuario()) && staticPassword.equals(dadosLogin.getSenha())) {
            String tokenJWT = jwtTokenService.gerarToken(dadosLogin.getUsuario());
            return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
        } else {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }
    }
}
