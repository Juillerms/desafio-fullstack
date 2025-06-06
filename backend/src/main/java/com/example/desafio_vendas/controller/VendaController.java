package com.example.desafio_vendas.controller;

import com.example.desafio_vendas.dto.CriarVendaDTO; // Importa o novo DTO
import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa PostMapping, RequestBody, etc.
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")
// A anotação @CrossOrigin foi removida daqui, pois agora é gerenciada globalmente no SecurityConfig.
public class VendaController {

    @Autowired
    private VendaService vendaService;

    /**
     * Lista vendas, com capacidade de filtro por data de início e data de fim.
     */
    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Venda> vendas = vendaService.listarVendasComFiltro(dataInicio, dataFim); // Passando null para produtoId por enquanto

        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(vendas);
    }

    /**
     * Busca uma venda específica pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        Venda venda = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(venda);
    }

    /**
     * Cria uma nova venda.
     * @param dados Os dados da nova venda, vindos do corpo da requisição.
     * @param uriBuilder Injetado pelo Spring para ajudar a construir a URI de resposta.
     * @return Uma resposta com status 201 Created, a URI do novo recurso no cabeçalho Location,
     * e o corpo da venda salva na resposta.
     */
    @PostMapping
    public ResponseEntity<Venda> criarVenda(@RequestBody CriarVendaDTO dados, UriComponentsBuilder uriBuilder) {
        // Converte o DTO para a entidade Venda
        Venda novaVenda = new Venda();
        novaVenda.setNomeProduto(dados.getNomeProduto());
        novaVenda.setQuantidadeVendida(dados.getQuantidadeVendida());
        novaVenda.setDataVenda(dados.getDataVenda());
        novaVenda.setValorTotal(dados.getValorTotal());

        Venda vendaSalva = vendaService.salvarVenda(novaVenda);

        // Constrói a URI para o novo recurso criado (boa prática REST)
        // Ex: http://localhost:8080/vendas/21
        URI uri = uriBuilder.path("/vendas/{id}").buildAndExpand(vendaSalva.getId()).toUri();

        // Retorna a resposta 201 Created
        return ResponseEntity.created(uri).body(vendaSalva);
    }
}