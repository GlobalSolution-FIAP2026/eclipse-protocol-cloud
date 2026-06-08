package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    boolean existsByPlantacaoId(Long id);
}
