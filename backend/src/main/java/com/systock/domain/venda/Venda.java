package com.systock.domain.venda;

import com.systock.domain.cliente.Cliente;
import com.systock.domain.produto.Produto;
import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.domain.usuario.Usuario;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero", nullable = false, unique = true, insertable = false, updatable = false)
    private Long numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusVenda status = StatusVenda.FINALIZADA;

    @Column(name = "realizado_em", nullable = false)
    private OffsetDateTime realizadoEm;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    protected Venda() {
    }

    public Venda(Usuario usuario, Cliente cliente) {
        this.usuario = Objects.requireNonNull(usuario, "usuário obrigatório");
        this.cliente = cliente;
        this.status = StatusVenda.FINALIZADA;
        this.realizadoEm = OffsetDateTime.now();
        this.criadoEm = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public Long getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public Usuario getUsuario() { return usuario; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusVenda getStatus() { return status; }
    public OffsetDateTime getRealizadoEm() { return realizadoEm; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (status == StatusVenda.CANCELADA) {
            throw new RegraDeNegocioException("Não é possível adicionar itens a uma venda cancelada");
        }
        if (!produto.isAtivo()) {
            throw new RegraDeNegocioException(
                    "Produto '" + produto.getNome() + "' está inativo e não pode ser vendido");
        }
        produto.darBaixaEstoque(quantidade);
        itens.add(new ItemVenda(this, produto, quantidade, produto.getPrecoVenda()));
        recalcularTotal();
    }

    public void finalizar() {
        if (itens.isEmpty()) {
            throw new RegraDeNegocioException("A venda deve conter ao menos um item");
        }
        recalcularTotal();
    }

    private void recalcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venda that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
