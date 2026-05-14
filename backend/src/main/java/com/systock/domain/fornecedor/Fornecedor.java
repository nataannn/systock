package com.systock.domain.fornecedor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Pattern(
        regexp = "\\d{14}",
        message = "O CNPJ deve conter 14 números"
    )
    @Column(name = "cnpj", nullable = false, length = 14, unique = true)
    private String cnpj;

    @NotBlank
    @Size(min = 2, max = 150)
    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Size(max = 150)
    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Email
    @Size(max = 120)
    @Column(name = "email", length = 120)
    private String email;

    @Size(max = 20)
    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    protected Fornecedor() {
    }

    public Fornecedor(String cnpj, String razaoSocial) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial.trim();
        this.ativo = true;
        this.criadoEm = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizarDadosCadastrais(
            String razaoSocial,
            String nomeFantasia
    ) {
        this.razaoSocial = razaoSocial.trim();

        this.nomeFantasia =
                (nomeFantasia == null || nomeFantasia.isBlank())
                        ? null
                        : nomeFantasia.trim();
    }

    public void atualizarContato(
            String email,
            String telefone
    ) {
        this.email =
                (email == null || email.isBlank())
                        ? null
                        : email.trim().toLowerCase();

        this.telefone =
                (telefone == null || telefone.isBlank())
                        ? null
                        : telefone.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fornecedor that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}