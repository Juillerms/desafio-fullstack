package com.example.desafio_vendas.repository;

import com.example.desafio_vendas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate; 
import java.util.List;     

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim);

    List<Venda> findByDataVendaGreaterThanEqual(LocalDate dataInicio);

    List<Venda> findByDataVendaLessThanEqual(LocalDate dataFim);

}