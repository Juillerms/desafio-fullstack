package com.example.desafio_vendas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DetalheErro {
    private String titulo;
    private int status;
    private String detalhe;
    private LocalDateTime timestamp;
    private String mensagemDesenvolvedor;
}
