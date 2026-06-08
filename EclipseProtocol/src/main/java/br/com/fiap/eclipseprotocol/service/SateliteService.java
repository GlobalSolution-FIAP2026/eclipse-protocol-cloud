package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.BusinessException;
import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.Satelite;
import br.com.fiap.eclipseprotocol.repository.ImagemSateliteRepository;
import br.com.fiap.eclipseprotocol.repository.RiscoOrbitalRepository;
import br.com.fiap.eclipseprotocol.repository.SateliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SateliteService {

    private final SateliteRepository repository;
    private final ImagemSateliteRepository imagemSateliteRepository;
    private final RiscoOrbitalRepository riscoOrbitalRepository;

    public SateliteService(SateliteRepository repository,
                           ImagemSateliteRepository imagemSateliteRepository,
                           RiscoOrbitalRepository riscoOrbitalRepository) {
        this.repository = repository;
        this.imagemSateliteRepository = imagemSateliteRepository;
        this.riscoOrbitalRepository = riscoOrbitalRepository;
    }

    public List<Satelite> listarTodos() {
        return repository.findAll();
    }

    public Satelite buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado"));
    }

    public Satelite salvar(Satelite satelite) {
        return repository.save(satelite);
    }

    public Satelite atualizar(Long id, Satelite sateliteAtualizado) {
        Satelite satelite = buscarPorId(id);

        satelite.setNome(sateliteAtualizado.getNome());
        satelite.setTipo(sateliteAtualizado.getTipo());
        satelite.setOrbita(sateliteAtualizado.getOrbita());
        satelite.setAltitudeKm(sateliteAtualizado.getAltitudeKm());
        satelite.setStatus(sateliteAtualizado.getStatus());
        satelite.setDataLancamento(sateliteAtualizado.getDataLancamento());

        return repository.save(satelite);
    }

    public void deletar(Long id) {
        Satelite satelite = buscarPorId(id);

        if (imagemSateliteRepository.existsBySateliteId(id)) {
            throw new BusinessException(
                    "Não é possível deletar este satélite, pois ele está vinculado a uma imagem."
            );
        }

        if (riscoOrbitalRepository.existsBySateliteId(id)) {
            throw new BusinessException(
                    "Não é possível deletar este satélite, pois ele está vinculado a um risco orbital."
            );
        }

        repository.delete(satelite);
    }
}

