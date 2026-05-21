package com.systock.domain.estoque;

import com.systock.domain.produto.Produto;
import com.systock.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {

    public enum TipoMovimentacao {
        ENTRADA, SAIDA
    }

    public enum ReferenciaTipo {
        VENDA, MANUAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "referencia_tipo", length = 20)
    private ReferenciaTipo referenciaTipo;

    @Column(name = "referencia_id")
    private UUID referenciaId;

    @Column(name = "realizado_em", nullable = false)
    private OffsetDateTime realizadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    protected MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(Produto produto,
                               TipoMovimentacao tipo,
                               int quantidade,
                               ReferenciaTipo referenciaTipo,
                               UUID referenciaId,
                               Usuario usuario) {
        this.produto = Objects.requireNonNull(produto);
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.referenciaTipo = referenciaTipo;
        this.referenciaId = referenciaId;
        this.usuario = usuario;
        this.realizadoEm = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public Produto getProduto() { return produto; }
    public TipoMovimentacao getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
    public ReferenciaTipo getReferenciaTipo() { return referenciaTipo; }
    public UUID getReferenciaId() { return referenciaId; }
    public OffsetDateTime getRealizadoEm() { return realizadoEm; }
    public Usuario getUsuario() { return usuario; }
}
