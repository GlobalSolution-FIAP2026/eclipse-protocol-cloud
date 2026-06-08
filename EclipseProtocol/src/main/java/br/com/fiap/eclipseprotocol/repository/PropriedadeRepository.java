package br.com.fiap.eclipseprotocol.repository;

import br.com.fiap.eclipseprotocol.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {

    boolean existsByLocalizacaoId(Long id);
    boolean existsByUsuarioId(Long id);
}
