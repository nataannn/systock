package com.systock.application.cliente;

import com.systock.domain.cliente.Cliente;
import com.systock.domain.shared.CpfValidator;
import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.infrastructure.crypto.DadosSensiveisCryptoService;
import com.systock.infrastructure.crypto.HashUtil;
import com.systock.infrastructure.persistence.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final DadosSensiveisCryptoService cryptoService;

    public ClienteService(ClienteRepository repository,
                          DadosSensiveisCryptoService cryptoService) {
        this.repository = repository;
        this.cryptoService = cryptoService;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return repository.findAllByOrderByNomeAsc().stream()
                .map(this::descriptografarParaExibicao)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarAtivos() {
        return repository.findAllByAtivoTrueOrderByNomeAsc().stream()
                .map(this::descriptografarParaExibicao)
                .toList();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(UUID id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Cliente não encontrado: " + id, "/clientes"));
        return descriptografarParaExibicao(cliente);
    }

    @Transactional
    public Cliente criar(String cpfBruto, String nome, String email, String telefone) {
        String cpf = normalizarCpf(cpfBruto);
        validarCpf(cpf);

        String cpfHash = HashUtil.sha256(cpf);
        if (repository.existsByCpfHash(cpfHash)) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente com este CPF", "/clientes");
        }

        String emailNormalizado = normalizarEmail(email);
        String emailHash = hashEmail(emailNormalizado);
        validarEmailUnico(emailHash, null);

        Cliente cliente = new Cliente(
                nome,
                cryptoService.criptografar(cpf),
                cpfHash,
                cryptoService.criptografar(emailNormalizado),
                emailHash,
                telefone);

        return descriptografarParaExibicao(repository.save(cliente));
    }

    @Transactional
    public Cliente atualizar(UUID id, String cpfBruto, String nome, String email, String telefone) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Cliente não encontrado: " + id, "/clientes"));

        String cpf = normalizarCpf(cpfBruto);
        validarCpf(cpf);
        String cpfHash = HashUtil.sha256(cpf);
        if (repository.existsByCpfHashAndIdNot(cpfHash, id)) {
            throw new RegraDeNegocioException(
                    "Já existe outro cliente com este CPF",
                    "/clientes/" + id + "/editar");
        }

        String emailNormalizado = normalizarEmail(email);
        String emailHash = hashEmail(emailNormalizado);
        validarEmailUnico(emailHash, id);

        cliente.atualizarDados(nome, telefone);
        cliente.atualizarDadosSensiveis(
                cryptoService.criptografar(cpf),
                cpfHash,
                cryptoService.criptografar(emailNormalizado),
                emailHash);

        return descriptografarParaExibicao(cliente);
    }

    @Transactional
    public void desativar(UUID id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Cliente não encontrado: " + id, "/clientes"));
        cliente.desativar();
    }

    @Transactional
    public void ativar(UUID id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Cliente não encontrado: " + id, "/clientes"));
        cliente.ativar();
    }

    private Cliente descriptografarParaExibicao(Cliente cliente) {
        String cpf = cryptoService.descriptografar(cliente.getCpfCriptografado());
        String email = cryptoService.descriptografar(cliente.getEmailCriptografado());
        cliente.popularDadosDescriptografados(cpf, email);
        return cliente;
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new RegraDeNegocioException("CPF deve conter exatamente 11 dígitos", "/clientes");
        }
        if (!CpfValidator.isValido(cpf)) {
            throw new RegraDeNegocioException(
                    "CPF inválido: dígitos verificadores não conferem", "/clientes");
        }
    }

    private void validarEmailUnico(String emailHash, UUID idExcluir) {
        if (emailHash == null) {
            return;
        }
        boolean duplicado = idExcluir == null
                ? repository.existsByEmailHash(emailHash)
                : repository.existsByEmailHashAndIdNot(emailHash, idExcluir);
        if (duplicado) {
            throw new RegraDeNegocioException(
                    "Já existe um cliente com este e-mail", "/clientes");
        }
    }

    private String normalizarCpf(String texto) {
        return texto == null ? null : texto.replaceAll("\\D", "");
    }

    private String normalizarEmail(String email) {
        return (email == null || email.isBlank()) ? null : email.trim().toLowerCase();
    }

    private String hashEmail(String emailNormalizado) {
        return emailNormalizado == null ? null : HashUtil.sha256(emailNormalizado);
    }
}
