package com.systock.application.dashboard;

public record DashboardMetrics(
        long totalCategorias,
        long totalFornecedoresAtivos,
        long totalProdutos,
        long produtosEmEstoqueCritico
) {
}