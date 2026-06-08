package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.BusinessException;
import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.LixoEspacial;
import br.com.fiap.eclipseprotocol.repository.LixoEspacialRepository;
import br.com.fiap.eclipseprotocol.repository.RiscoOrbitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LixoEspacialService {

    private final LixoEspacialRepository repository;
    private final RiscoOrbitalRepository riscoOrbitalRepository;

    public LixoEspacialService(LixoEspacialRepository repository,
                                RiscoOrbitalRepository riscoOrbitalRepository) {
        this.repository = repository;
        this.riscoOrbitalRepository = riscoOrbitalRepository;
    }

    public List<LixoEspacial> listarTodos() {
        return repository.findAll();
    }

    public LixoEspacial buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lixo espacial não encontrado"));
    }

    public LixoEspacial salvar(LixoEspacial lixoEspacial) {
        return repository.save(lixoEspacial);
    }

    public LixoEspacial atualizar(Long id, LixoEspacial lixoAtualizado) {
        LixoEspacial lixo = buscarPorId(id);

        lixo.setNomeObjeto(lixoAtualizado.getNomeObjeto());
        lixo.setTipoObjeto(lixoAtualizado.getTipoObjeto());
        lixo.setAltitudeKm(lixoAtualizado.getAltitudeKm());
        lixo.setVelocidadeKmh(lixoAtualizado.getVelocidadeKmh());
        lixo.setOrbita(lixoAtualizado.getOrbita());
        lixo.setDataIdentificacao(lixoAtualizado.getDataIdentificacao());

        return repository.save(lixo);
    }

    public void deletar(Long id) {
        LixoEspacial lixo = buscarPorId(id);

        if (riscoOrbitalRepository.existsByLixoEspacialId(id)) {
            throw new BusinessException(
                    "Não é possível deletar este objeto, pois ele está vinculado a um risco orbital."
            );
        }

        repository.delete(lixo);
    }
}

