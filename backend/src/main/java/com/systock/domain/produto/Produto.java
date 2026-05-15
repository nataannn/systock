package com.systock.domain.produto;

import com.systock.domain.categoria.Categoria;
import com.systock.domain.fornecedor.Fornecedor;
import com.systock.domain.shared.RegraDeNegocioException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 40)
    @Column(name = "codigo", nullable = false, length = 40, unique = true)
    private String codigo;

    @NotBlank
    @Size(min = 2, max = 150)
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Size(max = 500)
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "preco_custo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoCusto;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "preco_venda", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVenda;

    @Min(0)
    @Column(name = "estoque_atual", nullable = false)
    private int estoqueAtual;

    @Min(0)
    @Column(name = "estoque_minimo", nullable = false)
    private int estoqueMinimo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    protected Produto() {
    }

    public Produto(String codigo,
                   String nome,
                   BigDecimal precoCusto,
                   BigDecimal precoVenda,
                   int estoqueInicial,
                   int estoqueMinimo,
                   Categoria categoria,
                   Fornecedor fornecedor) {
        this.codigo = Objects.requireNonNull(codigo, "código obrigatório").trim();
        this.nome = Objects.requireNonNull(nome, "nome obrigatório").trim();
        this.precoCusto = Objects.requireNonNull(precoCusto, "preço de custo obrigatório");
        this.precoVenda = Objects.requireNonNull(precoVenda, "preço de venda obrigatório");
        if (estoqueInicial < 0) {
            throw new RegraDeNegocioException("Estoque inicial não pode ser negativo");
        }
        if (estoqueMinimo < 0) {
            throw new RegraDeNegocioException("Estoque mínimo não pode ser negativo");
        }
        this.estoqueAtual = estoqueInicial;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = Objects.requireNonNull(categoria, "categoria obrigatória");
        this.fornecedor = Objects.requireNonNull(fornecedor, "fornecedor obrigatório");
        this.ativo = true;
        this.criadoEm = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public int getEstoqueAtual() { return estoqueAtual; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public Categoria getCategoria() { return categoria; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }

    public boolean isEstoqueCritico() {
        return estoqueAtual <= estoqueMinimo;
    }

    public boolean isVendaAbaixoDoCusto() {
        return precoVenda.compareTo(precoCusto) < 0;
    }

    public void ativar() { this.ativo = true; }
    public void desativar() { this.ativo = false; }

    public void atualizarDadosCadastrais(String nome,
                                          String descricao,
                                          BigDecimal precoCusto,
                                          BigDecimal precoVenda,
                                          int estoqueMinimo,
                                          Categoria categoria,
                                          Fornecedor fornecedor) {
        this.nome = Objects.requireNonNull(nome, "nome obrigatório").trim();
        this.descricao = (descricao == null || descricao.isBlank()) ? null : descricao.trim();
        this.precoCusto = Objects.requireNonNull(precoCusto, "preço de custo obrigatório");
        this.precoVenda = Objects.requireNonNull(precoVenda, "preço de venda obrigatório");
        if (estoqueMinimo < 0) {
            throw new RegraDeNegocioException("Estoque mínimo não pode ser negativo");
        }
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = Objects.requireNonNull(categoria, "categoria obrigatória");
        this.fornecedor = Objects.requireNonNull(fornecedor, "fornecedor obrigatório");
    }

    public void darEntradaEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraDeNegocioException("Quantidade de entrada deve ser positiva");
        }
        this.estoqueAtual += quantidade;
    }

    public void darBaixaEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraDeNegocioException("Quantidade de baixa deve ser positiva");
        }
        if (quantidade > estoqueAtual) {
            throw new RegraDeNegocioException(
                    "Estoque insuficiente do produto '" + nome + "': disponível " 
                    + estoqueAtual + ", solicitado " + quantidade);
        }
        this.estoqueAtual -= quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}