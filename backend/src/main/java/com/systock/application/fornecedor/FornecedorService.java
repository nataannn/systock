package com.systock.application.fornecedor;

import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.domain.fornecedor.Fornecedor;
import com.systock.domain.shared.CnpjValidator;
import com.systock.infrastructure.persistence.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listarAtivos() {
        return repository.findAllByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Fornecedor não encontrado: " + id, "/fornecedores"));
    }

    @Transactional
    public Fornecedor criar(String cnpjBruto,
                            String razaoSocial,
                            String nomeFantasia,
                            String email,
                            String telefone) {
        String cnpj = somenteDigitos(cnpjBruto);
        if (cnpj == null || cnpj.length() != 14) {
            throw new RegraDeNegocioException(
                    "CNPJ deve conter exatamente 14 dígitos", "/fornecedores");
        }

        if (!CnpjValidator.isValido(cnpj)) {
            throw new RegraDeNegocioException(
                    "CNPJ inválido: dígitos verificadores não conferem", "/fornecedores");
        }

        if (repository.existsByCnpj(cnpj)) {
            throw new RegraDeNegocioException(
                    "Já existe um fornecedor com o CNPJ " + cnpj, "/fornecedores");
        }
        Fornecedor fornecedor = new Fornecedor(cnpj, razaoSocial);
        fornecedor.atualizarDadosCadastrais(razaoSocial, nomeFantasia);
        fornecedor.atualizarContato(email, telefone);
        return repository.save(fornecedor);
    }

    @Transactional
    public Fornecedor atualizar(UUID id,
                                String razaoSocial,
                                String nomeFantasia,
                                String email,
                                String telefone) {
        Fornecedor fornecedor = buscarPorId(id);
        fornecedor.atualizarDadosCadastrais(razaoSocial, nomeFantasia);
        fornecedor.atualizarContato(email, telefone);
        return fornecedor;
    }

    @Transactional
    public void desativar(UUID id) {
        Fornecedor fornecedor = buscarPorId(id);
        fornecedor.desativar();
    }

    @Transactional
    public void ativar(UUID id) {
        Fornecedor fornecedor = buscarPorId(id);
        fornecedor.ativar();
    }

    private String somenteDigitos(String texto) {
        return texto == null ? null : texto.replaceAll("\\D", "");
    }
}