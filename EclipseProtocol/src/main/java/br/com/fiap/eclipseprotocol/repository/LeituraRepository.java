package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.Leitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeituraRepository extends JpaRepository<Leitura, Long> {
    boolean existsBySensorId(Long id);
}
