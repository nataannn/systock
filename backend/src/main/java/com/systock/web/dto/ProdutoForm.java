package com.systock.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class ProdutoForm {

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 2, max = 40)
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 150)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull(message = "Preço de custo é obrigatório")
    @DecimalMin(value = "0.00", message = "Preço de custo não pode ser negativo")
    private BigDecimal precoCusto;

    @NotNull(message = "Preço de venda é obrigatório")
    @DecimalMin(value = "0.00", message = "Preço de venda não pode ser negativo")
    private BigDecimal precoVenda;

    @NotNull(message = "Estoque inicial é obrigatório")
    @PositiveOrZero(message = "Estoque não pode ser negativo")
    private Integer estoqueAtual;

    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
    private Integer estoqueMinimo;

    @NotNull(message = "Selecione uma categoria")
    private UUID categoriaId;

    @NotNull(message = "Selecione um fornecedor")
    private UUID fornecedorId;

    // Getters e setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public Integer getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(Integer estoqueAtual) { this.estoqueAtual = estoqueAtual; }
    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    public UUID getCategoriaId() { return categoriaId; }
    public void setCategoriaId(UUID categoriaId) { this.categoriaId = categoriaId; }
    public UUID getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(UUID fornecedorId) { this.fornecedorId = fornecedorId; }
}