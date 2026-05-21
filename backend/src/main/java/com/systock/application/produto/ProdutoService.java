package com.systock.application.produto;

import com.systock.domain.categoria.Categoria;
import com.systock.domain.fornecedor.Fornecedor;
import com.systock.domain.produto.Produto;
import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.infrastructure.persistence.CategoriaRepository;
import com.systock.infrastructure.persistence.FornecedorRepository;
import com.systock.infrastructure.persistence.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository,
                          FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.listarTodosComCategoriaEFornecedor();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarAtivos() {
        return produtoRepository.listarAtivosComCategoriaEFornecedor();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Produto não encontrado: " + id, "/produtos"));
    }

    @Transactional
    public Produto criar(String codigo,
                         String nome,
                         String descricao,
                         BigDecimal precoCusto,
                         BigDecimal precoVenda,
                         int estoqueInicial,
                         int estoqueMinimo,
                         UUID categoriaId,
                         UUID fornecedorId) {

        String codigoNormalizado = codigo.trim().toUpperCase();

        if (produtoRepository.existsByCodigoIgnoreCase(codigoNormalizado)) {
            throw new RegraDeNegocioException(
                    "Já existe um produto com o código " + codigoNormalizado, "/produtos/novo");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Categoria selecionada não existe", "/produtos/novo"));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Fornecedor selecionado não existe", "/produtos/novo"));

        if (!fornecedor.isAtivo()) {
            throw new RegraDeNegocioException(
                    "Não é possível cadastrar produto vinculado a fornecedor inativo",
                    "/produtos/novo");
        }

        Produto produto = new Produto(
                codigoNormalizado, nome, precoCusto, precoVenda,
                estoqueInicial, estoqueMinimo, categoria, fornecedor);

        if (descricao != null && !descricao.isBlank()) {
            produto.atualizarDadosCadastrais(
                    nome, descricao, precoCusto, precoVenda,
                    estoqueMinimo, categoria, fornecedor);
        }

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(UUID id,
                             String nome,
                             String descricao,
                             BigDecimal precoCusto,
                             BigDecimal precoVenda,
                             int estoqueMinimo,
                             UUID categoriaId,
                             UUID fornecedorId) {

        Produto produto = buscarPorId(id);

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Categoria selecionada não existe", "/produtos/" + id + "/editar"));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Fornecedor selecionado não existe", "/produtos/" + id + "/editar"));

        produto.atualizarDadosCadastrais(
                nome, descricao, precoCusto, precoVenda,
                estoqueMinimo, categoria, fornecedor);

        return produto;
    }

    @Transactional
    public void desativar(UUID id) {
        Produto produto = buscarPorId(id);
        produto.desativar();
    }

    @Transactional
    public void ativar(UUID id) {
        Produto produto = buscarPorId(id);
        produto.ativar();
    }

    @Transactional
    public Produto darEntradaEstoque(UUID id, int quantidade) {
        Produto produto = buscarPorId(id);
        produto.darEntradaEstoque(quantidade);
        return produto;
    }

    @Transactional
    public Produto darBaixaEstoque(UUID id, int quantidade) {
        Produto produto = buscarPorId(id);
        produto.darBaixaEstoque(quantidade);
        return produto;
    }
}