package com.systock.domain.categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(name = "nome", nullable = false, length = 80, unique = true)
    private String nome;

    // JPA exige construtor sem argumentos. Não usar diretamente no código
    protected Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void alterarNome(String novoNome) {
        this.nome = novoNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Categoria other)) return false;
        return id != null & id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}