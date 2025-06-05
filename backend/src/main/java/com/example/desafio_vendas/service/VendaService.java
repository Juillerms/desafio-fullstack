package com.example.desafio_vendas.service;

import com.example.desafio_vendas.exception.ResourceNotFoundException;
import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate; // Importe LocalDate
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    @Autowired
    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    /**
     * Lista todas as vendas, aplicando filtros de data se fornecidos.
     *
     * @param dataInicio Data de início para o filtro (pode ser null).
     * @param dataFim    Data de fim para o filtro (pode ser null).
     * @return Lista de vendas filtrada ou todas as vendas se nenhum filtro for aplicado.
     */
    public List<Venda> listarVendasComFiltro(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null) {
            // Se ambas as datas são fornecidas, busca no intervalo
            return vendaRepository.findByDataVendaBetween(dataInicio, dataFim);
        } else if (dataInicio != null) {
            // Se apenas a data de início é fornecida, busca a partir dessa data
            return vendaRepository.findByDataVendaGreaterThanEqual(dataInicio);
        } else if (dataFim != null) {
            // Se apenas a data de fim é fornecida, busca até essa data
            return vendaRepository.findByDataVendaLessThanEqual(dataFim);
        } else {
            // Se nenhuma data é fornecida, retorna todas as vendas
            return vendaRepository.findAll();
        }
    }

    // O método listarTodasAsVendas() original pode ser mantido se você quiser uma forma
    // explícita de buscar todas sem passar null para o método de filtro,
    // ou pode ser removido se listarVendasComFiltro(null, null) for o suficiente.
    // Por ora, vou mantê-lo como no seu código original.
    public List<Venda> listarTodasAsVendas() {
        return vendaRepository.findAll();
    }

    public Venda salvarVenda(Venda venda) {
        // Adicionar validações de negócio aqui se necessário antes de salvar
        return vendaRepository.save(venda);
    }

    public Venda buscarVendaPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda com ID " + id + " não foi encontrada."));
    }

    public void deletarVenda(Long id) {
        // Adicionar verificação se a venda existe antes de deletar, para lançar ResourceNotFoundException
        if (!vendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venda com ID " + id + " não encontrada, não é possível deletar.");
        }
        vendaRepository.deleteById(id);
    }
}