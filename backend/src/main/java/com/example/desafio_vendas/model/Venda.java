package com.example.desafio_vendas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity 
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Venda {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String nomeProduto;
    private int quantidadeVendida;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
}
