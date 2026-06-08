package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.BusinessException;
import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.Propriedade;
import br.com.fiap.eclipseprotocol.repository.PlantacaoRepository;
import br.com.fiap.eclipseprotocol.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropriedadeService {

    private final PropriedadeRepository repository;
    private final PlantacaoRepository  plantacaoRepository;

    public PropriedadeService(PropriedadeRepository repository,
                              PlantacaoRepository plantacaoRepository) {
        this.repository = repository;
        this.plantacaoRepository = plantacaoRepository;
    }

    public List<Propriedade> listarTodos() {
        return repository.findAll();
    }

    public Propriedade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada"));
    }

    public Propriedade salvar(Propriedade propriedade) {
        return repository.save(propriedade);
    }

    public Propriedade atualizar(Long id, Propriedade propriedadeAtualizada) {
        Propriedade propriedade = buscarPorId(id);

        propriedade.setNome(propriedadeAtualizada.getNome());
        propriedade.setProprietario(propriedadeAtualizada.getProprietario());
        propriedade.setAreaTotal(propriedadeAtualizada.getAreaTotal());
        propriedade.setTipoSolo(propriedadeAtualizada.getTipoSolo());
        propriedade.setLocalizacao(propriedadeAtualizada.getLocalizacao());
        propriedade.setUsuario(propriedadeAtualizada.getUsuario());

        return repository.save(propriedade);
    }

    public void deletar(Long id) {
        Propriedade propriedade = buscarPorId(id);

        if (plantacaoRepository.existsByPropriedadeId(id)) {
            throw new BusinessException(
                    "Não é possível deletar esta propriedade, pois ela está vinculada a uma plantação."
            );
        }

        repository.delete(propriedade);
    }
}