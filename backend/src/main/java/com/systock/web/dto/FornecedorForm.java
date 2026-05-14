package com.systock.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FornecedorForm {

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(
            regexp = "\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
            message = "CNPJ deve ter 14 dígitos (com ou sem máscara)"
    )
    private String cnpj;

    @NotBlank(message = "A razão social é obrigatória")
    @Size(min = 2, max = 150, message = "Razão social deve ter entre 2 e 150 caracteres")
    private String razaoSocial;

    @Size(max = 150, message = "Nome fantasia não pode passar de 150 caracteres")
    private String nomeFantasia;

    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    @Size(max = 20, message = "Telefone não pode passar de 20 caracteres")
    private String telefone;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}