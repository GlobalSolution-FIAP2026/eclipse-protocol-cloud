package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.BusinessException;
import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.Plantacao;
import br.com.fiap.eclipseprotocol.repository.PlantacaoRepository;
import br.com.fiap.eclipseprotocol.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantacaoService {
    private final PlantacaoRepository repository;
    private final SensorRepository sensorRepository;

    public PlantacaoService(PlantacaoRepository repository,
                            SensorRepository sensorRepository) {
        this.repository = repository;
        this.sensorRepository = sensorRepository;
    }

    public List<Plantacao> listarTodos(){
        return repository.findAll();
    }

    public Plantacao buscarPorId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plantação não encontrada"));
    }

    public Plantacao salvar(Plantacao plantacao){
        return repository.save(plantacao);
    }

    public Plantacao atualizar(Long id, Plantacao plantacaoAtualizada){
        Plantacao plantacao =  buscarPorId(id);

        plantacao.setNome(plantacaoAtualizada.getNome());
        plantacao.setCultura(plantacaoAtualizada.getCultura());
        plantacao.setAreaHectares(plantacaoAtualizada.getAreaHectares());
        plantacao.setStatus(plantacaoAtualizada.getStatus());
        plantacao.setPropriedade(plantacaoAtualizada.getPropriedade());

        return repository.save(plantacao);
    }

    public void deletar(Long id) {
        Plantacao plantacao = buscarPorId(id);

        if (sensorRepository.existsByPlantacaoId(id)) {
            throw new BusinessException(
                    "Não é possível deletar esta plantação, pois ela está vinculada a um sensor."
            );
        }

        repository.delete(plantacao);
    }
}
