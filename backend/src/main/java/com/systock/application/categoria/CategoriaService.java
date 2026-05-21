package com.systock.application.categoria;

import com.systock.domain.categoria.Categoria;
import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.infrastructure.persistence.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Categoria não encontrada: " + id, "/categorias"));
    }

    @Transactional
    public Categoria criar(String nome) {
        String nomeNormalizado = nome.trim();
        if (repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegraDeNegocioException(
                    "Já existe uma categoria com o nome '" + nomeNormalizado + "'", "/categorias");
        }
        Categoria nova = new Categoria(nomeNormalizado);
        return repository.save(nova);
    }

    @Transactional
    public Categoria alterarNome(UUID id, String novoNome) {
        String nomeNormalizado = novoNome.trim();
        Categoria categoria = buscarPorId(id);
        if (!categoria.getNome().equalsIgnoreCase(nomeNormalizado)
                && repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegraDeNegocioException(
                    "Já existe outra categoria com o nome '" + nomeNormalizado + "'",
                    "/categorias/" + id + "/editar");
        }
        categoria.alterarNome(nomeNormalizado);
        return categoria;
    }

    @Transactional
    public void excluir(UUID id) {
        Categoria categoria = buscarPorId(id);
        repository.delete(categoria);
    }
}