package com.systock.domain.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf_criptografado", nullable = false, columnDefinition = "TEXT")
    private String cpfCriptografado;

    @Column(name = "cpf_hash", nullable = false, length = 64, unique = true)
    private String cpfHash;

    @Column(name = "email_criptografado", columnDefinition = "TEXT")
    private String emailCriptografado;

    @Column(name = "email_hash", length = 64, unique = true)
    private String emailHash;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @Transient
    private String cpf;

    @Transient
    private String email;

    protected Cliente() {
    }

    public Cliente(String nome,
                   String cpfCriptografado,
                   String cpfHash,
                   String emailCriptografado,
                   String emailHash,
                   String telefone) {
        this.nome = nome.trim();
        this.cpfCriptografado = cpfCriptografado;
        this.cpfHash = cpfHash;
        this.emailCriptografado = emailCriptografado;
        this.emailHash = emailHash;
        this.telefone = normalizarTelefone(telefone);
        this.ativo = true;
        this.criadoEm = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getCpfCriptografado() { return cpfCriptografado; }
    public String getCpfHash() { return cpfHash; }
    public String getEmailCriptografado() { return emailCriptografado; }
    public String getEmailHash() { return emailHash; }
    public String getTelefone() { return telefone; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }

    public void popularDadosDescriptografados(String cpf, String email) {
        this.cpf = cpf;
        this.email = email;
    }

    public String getCpfMascarado() {
        if (cpf == null || cpf.length() != 11) {
            return "***.***.***-**";
        }
        return "***.***.***-" + cpf.substring(9);
    }

    public void ativar() { this.ativo = true; }
    public void desativar() { this.ativo = false; }

    public void atualizarDados(String nome, String telefone) {
        this.nome = nome.trim();
        this.telefone = normalizarTelefone(telefone);
    }

    public void atualizarDadosSensiveis(String cpfCriptografado,
                                        String cpfHash,
                                        String emailCriptografado,
                                        String emailHash) {
        this.cpfCriptografado = cpfCriptografado;
        this.cpfHash = cpfHash;
        this.emailCriptografado = emailCriptografado;
        this.emailHash = emailHash;
    }

    private static String normalizarTelefone(String telefone) {
        return (telefone == null || telefone.isBlank()) ? null : telefone.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
