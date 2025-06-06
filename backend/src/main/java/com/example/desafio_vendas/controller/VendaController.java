package com.example.desafio_vendas.controller;

import com.example.desafio_vendas.dto.CriarVendaDTO; 
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Vendas", description = "Operações para listar, criar, buscar e deletar vendas.")
@SecurityRequirement(name = "bearerAuth") // Aplica a exigência de token em todos os endpoints deste controller
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    @Operation(summary = "Lista todas as vendas com filtros opcionais",
               description = "Retorna uma lista de vendas. Pode ser filtrada por um intervalo de datas.")
    public ResponseEntity<List<Venda>> listarVendas(
            @Parameter(description = "Data de início do filtro (formato: YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data de fim do filtro (formato: YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<Venda> vendas = vendaService.listarVendasComFiltro(dataInicio, dataFim);

        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma venda por ID")
    @ApiResponse(responseCode = "200", description = "Venda encontrada.")
    @ApiResponse(responseCode = "404", description = "Venda não encontrada para o ID fornecido.")
    public ResponseEntity<Venda> buscarVendaPorId(@Parameter(description = "ID da venda a ser buscada") @PathVariable Long id) {
        Venda venda = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(venda);
    }

    @PostMapping
    @Operation(summary = "Cria uma nova venda")
    @ApiResponse(responseCode = "201", description = "Venda criada com sucesso.")
    public ResponseEntity<Venda> criarVenda(@RequestBody CriarVendaDTO dados, UriComponentsBuilder uriBuilder) {
        Venda novaVenda = new Venda();
        novaVenda.setNomeProduto(dados.getNomeProduto());
        novaVenda.setQuantidadeVendida(dados.getQuantidadeVendida());
        novaVenda.setDataVenda(dados.getDataVenda());
        novaVenda.setValorTotal(dados.getValorTotal());

        Venda vendaSalva = vendaService.salvarVenda(novaVenda);
        URI uri = uriBuilder.path("/vendas/{id}").buildAndExpand(vendaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(vendaSalva);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma venda por ID")
    @ApiResponse(responseCode = "204", description = "Venda deletada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Venda não encontrada para o ID fornecido.")
    public ResponseEntity<Void> deletarVenda(@Parameter(description = "ID da venda a ser deletada") @PathVariable Long id) {
        vendaService.deletarVenda(id);
        return ResponseEntity.noContent().build();
    }
}