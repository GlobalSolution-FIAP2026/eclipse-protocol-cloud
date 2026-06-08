package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    boolean existsByLeituraId(Long id);
}
