package com.example.desafio_vendas.repository;

import com.example.desafio_vendas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate; // Importe LocalDate
import java.util.List;     // Importe List

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    // Encontra vendas cuja dataVenda está entre dataInicio e dataFim (inclusivo)
    List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim);

    // Encontra vendas cuja dataVenda é maior ou igual a dataInicio
    List<Venda> findByDataVendaGreaterThanEqual(LocalDate dataInicio);

    // Encontra vendas cuja dataVenda é menor ou igual a dataFim
    List<Venda> findByDataVendaLessThanEqual(LocalDate dataFim);

    // Você também pode adicionar combinações mais complexas ou usar @Query se necessário,
    // mas para os filtros básicos de dataInicio e dataFim, os métodos acima geralmente bastam
    // e a lógica de qual usar pode ficar no Service.
}