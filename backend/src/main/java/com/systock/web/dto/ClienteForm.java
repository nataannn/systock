package com.systock.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteForm {

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(
            regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve ter 11 dígitos (com ou sem máscara)"
    )
    private String cpf;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String nome;

    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    @Size(max = 20, message = "Telefone não pode passar de 20 caracteres")
    private String telefone;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
