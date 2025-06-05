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

@Entity // Anotação para JPA
@Getter
@Setter
@NoArgsConstructor // Construtor padrão para JPA
@AllArgsConstructor
public class Venda {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremento
    private Long id;
    private String nomeProduto;
    private int quantidadeVendida;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
}
