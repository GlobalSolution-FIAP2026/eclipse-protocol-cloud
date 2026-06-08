package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.RiscoOrbital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiscoOrbitalRepository extends JpaRepository<RiscoOrbital, RiscoOrbital.RiscoOrbitalId> {
    boolean existsBySateliteId(Long id);
    boolean existsByLixoEspacialId(Long id);
}

