package com.example.desafio_vendas.config;

import com.example.desafio_vendas.model.Venda;
import com.example.desafio_vendas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VendaRepository vendaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (vendaRepository.count() == 0) { // Popula somente se o banco estiver vazio
            popularVendasSimuladas();
        }
    }

    private void popularVendasSimuladas() {
        vendaRepository.save(new Venda(null, "Notebook Gamer X", 2, LocalDate.now().minusDays(5), new BigDecimal("7500.00")));
        vendaRepository.save(new Venda(null, "Monitor LED 27\"", 5, LocalDate.now().minusDays(3), new BigDecimal("1250.50")));
        vendaRepository.save(new Venda(null, "Teclado Mecânico RGB", 10, LocalDate.now().minusDays(1), new BigDecimal("350.75")));
        vendaRepository.save(new Venda(null, "Mouse Sem Fio Ergonômico", 8, LocalDate.now().minusDays(10), new BigDecimal("150.00")));
        vendaRepository.save(new Venda(null, "SSD 1TB NVMe", 3, LocalDate.now().minusDays(2), new BigDecimal("600.00")));

        String[] produtos = {"Smartphone Top", "Cadeira Gamer Confort", "Headset Pro", "Webcam Full HD", "Placa de Vídeo RTX"};
        for (long i = 0; i < 15; i++) { // Gerar mais 15 vendas
            String produto = produtos[ThreadLocalRandom.current().nextInt(produtos.length)];
            int quantidade = ThreadLocalRandom.current().nextInt(1, 11);
            LocalDate data = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(1, 31));
            BigDecimal precoUnitario = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(100, 2000)).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
            vendaRepository.save(new Venda(null, produto, quantidade, data, valorTotal));
        }
        System.out.println(">>> Banco de dados H2 populado com dados de vendas simuladas.");
    }
}
