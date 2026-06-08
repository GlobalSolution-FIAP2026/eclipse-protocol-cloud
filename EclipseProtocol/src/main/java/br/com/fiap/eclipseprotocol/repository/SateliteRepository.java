package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.Satelite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SateliteRepository extends JpaRepository<Satelite, Long> {
    boolean existsByNome(String nome);
}

