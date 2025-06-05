package com.example.desafio_vendas.controller;

import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "http://localhost:3000")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    /**
     .
     *
     * @param dataInicio Data de início do filtro (formato YYYY-MM-DD).
     * @param dataFim    Data de fim do filtro (formato YYYY-MM-DD).
     * @return 
     */
    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Venda> vendas = vendaService.listarVendasComFiltro(dataInicio, dataFim);

        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 se a lista estiver vazia
        }
        
        return ResponseEntity.ok(vendas);
    }

    /**
     * Busca uma venda específica pelo seu ID.
     * @param id O ID da venda a ser buscada.
     * @return ResponseEntity contendo a Venda se encontrada, ou levará a um 404
     * (via GlobalExceptionHandler para ResourceNotFoundException) se não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        // em uma resposta HTTP 404 adequada.
        Venda venda = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(venda);
    }
}
