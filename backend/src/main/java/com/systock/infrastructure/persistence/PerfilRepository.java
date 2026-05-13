package com.systock.infrastructure.persistence;

import com.systock.domain.usuario.Perfil;
import com.systock.domain.usuario.TipoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, UUID> {

    Optional<Perfil> findByNome(TipoPerfil nome);
}