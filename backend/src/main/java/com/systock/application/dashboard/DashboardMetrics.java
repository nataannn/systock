package com.systock.application.dashboard;

public record DashboardMetrics(
        long totalCategorias,
        long totalFornecedoresAtivos,
        long totalProdutos,
        long produtosEmEstoqueCritico,
        long totalClientesAtivos,
        long totalVendas,
        java.math.BigDecimal valorVendasMes
) {
}