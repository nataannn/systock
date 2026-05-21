package com.systock.infrastructure.persistence;

import com.systock.domain.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {

    @Query("""
           SELECT DISTINCT v
           FROM Venda v
           LEFT JOIN FETCH v.cliente
           JOIN FETCH v.usuario u
           JOIN FETCH u.perfil
           LEFT JOIN FETCH v.itens i
           LEFT JOIN FETCH i.produto
           WHERE v.id = :id
           """)
    Optional<Venda> buscarDetalhadaPorId(UUID id);

    @Query("""
           SELECT v
           FROM Venda v
           LEFT JOIN FETCH v.cliente
           JOIN FETCH v.usuario
           ORDER BY v.realizadoEm DESC
           """)
    List<Venda> listarTodasComClienteEUsuario();

    long countByRealizadoEmAfter(OffsetDateTime desde);

    @Query("""
           SELECT COALESCE(SUM(v.valorTotal), 0)
           FROM Venda v
           WHERE v.status = com.systock.domain.venda.StatusVenda.FINALIZADA
             AND v.realizadoEm >= :desde
           """)
    BigDecimal somarValorDesde(OffsetDateTime desde);
}
