package com.systock.domain.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true, length = 32)
    @Enumerated(EnumType.STRING)
    private TipoPerfil nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    protected Perfil() {
    }

    public Perfil(TipoPerfil nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public TipoPerfil getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna a string da autoridade conforme convenção do Spring Security:
     * prefixo ROLE_ + nome do enum.
     */
    public String getAuthority() {
        return "ROLE_" + nome.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}