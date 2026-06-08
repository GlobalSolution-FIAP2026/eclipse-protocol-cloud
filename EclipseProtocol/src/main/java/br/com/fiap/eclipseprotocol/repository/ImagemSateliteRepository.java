package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.ImagemSatelite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagemSateliteRepository extends JpaRepository<ImagemSatelite, Long> {
    boolean existsBySateliteId(Long id);
    boolean existsByPlantacaoId(Long id);
}

