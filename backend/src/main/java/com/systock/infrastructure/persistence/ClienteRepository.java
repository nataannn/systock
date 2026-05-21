package com.systock.infrastructure.persistence;

import com.systock.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByCpfHash(String cpfHash);

    boolean existsByEmailHash(String emailHash);

    boolean existsByCpfHashAndIdNot(String cpfHash, UUID id);

    boolean existsByEmailHashAndIdNot(String emailHash, UUID id);

    List<Cliente> findAllByAtivoTrueOrderByNomeAsc();

    List<Cliente> findAllByOrderByNomeAsc();
}
