package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.LixoEspacial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LixoEspacialRepository extends JpaRepository<LixoEspacial, Long> {
    boolean existsByNomeObjeto(String nomeObjeto);
}

