package com.example.desafio_vendas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Venda {
    private Long id;
    private String nomeProduto;
    private int quantidadeVendida;
    private LocalDate dataVenda;
    private BigDecimal valorTotal; // Opcional, mas recomendado
}