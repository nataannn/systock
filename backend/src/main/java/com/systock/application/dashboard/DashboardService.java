package com.systock.application.dashboard;

import com.systock.infrastructure.persistence.CategoriaRepository;
import com.systock.infrastructure.persistence.ClienteRepository;
import com.systock.infrastructure.persistence.FornecedorRepository;
import com.systock.infrastructure.persistence.ProdutoRepository;
import com.systock.infrastructure.persistence.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class DashboardService {

    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;

    public DashboardService(CategoriaRepository categoriaRepository,
                             FornecedorRepository fornecedorRepository,
                             ProdutoRepository produtoRepository,
                             ClienteRepository clienteRepository,
                             VendaRepository vendaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    @Transactional(readOnly = true)
    public DashboardMetrics carregarMetricas() {
        long totalCategorias = categoriaRepository.count();
        long totalFornecedoresAtivos = fornecedorRepository.findAllByAtivoTrue().size();
        long totalProdutos = produtoRepository.count();
        long produtosEmEstoqueCritico = produtoRepository.listarAtivosComCategoriaEFornecedor()
                .stream()
                .filter(p -> p.isEstoqueCritico())
                .count();
        long totalClientesAtivos = clienteRepository.findAllByAtivoTrueOrderByNomeAsc().size();
        long totalVendas = vendaRepository.count();

        OffsetDateTime inicioMes = OffsetDateTime.now(ZoneOffset.UTC)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        BigDecimal valorVendasMes = vendaRepository.somarValorDesde(inicioMes);

        return new DashboardMetrics(
                totalCategorias,
                totalFornecedoresAtivos,
                totalProdutos,
                produtosEmEstoqueCritico,
                totalClientesAtivos,
                totalVendas,
                valorVendasMes
        );
    }
}