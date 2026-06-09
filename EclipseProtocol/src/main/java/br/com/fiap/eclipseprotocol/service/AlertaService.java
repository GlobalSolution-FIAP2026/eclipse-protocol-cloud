package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.Alerta;
import br.com.fiap.eclipseprotocol.repository.AlertaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository repository;

    public AlertaService(AlertaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Alerta> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Alerta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
    }

    @Transactional
    public Alerta salvar(Alerta alerta) {
        return repository.save(alerta);
    }

    @Transactional
    public Alerta atualizar(Long id, Alerta alertaAtualizado) {
        Alerta alerta = buscarPorId(id);

        alerta.setTipoAlerta(alertaAtualizado.getTipoAlerta());
        alerta.setSeveridade(alertaAtualizado.getSeveridade());
        alerta.setMensagem(alertaAtualizado.getMensagem());
        alerta.setStatus(alertaAtualizado.getStatus());
        alerta.setDataCriacao(alertaAtualizado.getDataCriacao());
        alerta.setLeitura(alertaAtualizado.getLeitura());
        alerta.setPlantacao(alertaAtualizado.getPlantacao());

        return repository.save(alerta);
    }

    @Transactional
    public void deletar(Long id) {
        Alerta alerta = buscarPorId(id);
        repository.delete(alerta);
    }
}
