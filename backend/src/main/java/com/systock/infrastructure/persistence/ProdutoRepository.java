package com.systock.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.systock.domain.produto.Produto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    Optional<Produto> findByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);

    @Query("""
           SELECT p
           FROM Produto p
           JOIN FETCH p.categoria
           JOIN FETCH p.fornecedor
           WHERE p.ativo = true
           ORDER BY p.nome
           """)
    List<Produto> listarAtivosComCategoriaEFornecedor();

    @Query("""
           SELECT p
           FROM Produto p
           JOIN FETCH p.categoria
           JOIN FETCH p.fornecedor
           ORDER BY p.nome
           """)
    List<Produto> listarTodosComCategoriaEFornecedor();
}