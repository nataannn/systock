package com.systock.application.dashboard;

import com.systock.infrastructure.persistence.CategoriaRepository;
import com.systock.infrastructure.persistence.FornecedorRepository;
import com.systock.infrastructure.persistence.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public DashboardService(CategoriaRepository categoriaRepository,
                             FornecedorRepository fornecedorRepository,
                             ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
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

        return new DashboardMetrics(
                totalCategorias,
                totalFornecedoresAtivos,
                totalProdutos,
                produtosEmEstoqueCritico
        );
    }
}