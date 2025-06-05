package com.example.desafio_vendas.service;

import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.repository.VendaRepository; // 1. Importe o repository
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    @Autowired
    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public List<Venda> listarTodasAsVendas() {
        return vendaRepository.findAll();
    }

    public Venda salvarVenda(Venda venda) {
        return vendaRepository.save(venda);
    }

    public Venda buscarVendaPorId(Long id) {
        return vendaRepository.findById(id).orElse(null);
    }

    public void deletarVenda(Long id) {
        vendaRepository.deleteById(id);
    }

}
