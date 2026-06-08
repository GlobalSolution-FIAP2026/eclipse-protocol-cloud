package br.com.fiap.eclipseprotocol.repository;


import br.com.fiap.eclipseprotocol.model.Plantacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantacaoRepository extends JpaRepository<Plantacao, Long> {
    boolean existsByPropriedadeId(Long id);
}
