package com.example.desafio_vendas.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CriarVendaDTO {
    private String nomeProduto;
    private int quantidadeVendida;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
}
