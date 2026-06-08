package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.BusinessException;
import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.Localizacao;
import br.com.fiap.eclipseprotocol.repository.LocalizacaoRepository;
import br.com.fiap.eclipseprotocol.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizacaoService {

    private final LocalizacaoRepository repository;
    private final PropriedadeRepository propriedadeRepository;

    public LocalizacaoService(
            LocalizacaoRepository repository,
            PropriedadeRepository propriedadeRepository
    ) {
        this.repository = repository;
        this.propriedadeRepository = propriedadeRepository;
    }

    public List<Localizacao> listarTodos() {
        return repository.findAll();
    }

    public Localizacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localização não encontrada"));
    }

    public Localizacao salvar(Localizacao localizacao) {
        return repository.save(localizacao);
    }

    public Localizacao atualizar(Long id, Localizacao localizacaoAtualizada) {
        Localizacao localizacao = buscarPorId(id);

        localizacao.setCidade(localizacaoAtualizada.getCidade());
        localizacao.setEstado(localizacaoAtualizada.getEstado());
        localizacao.setPais(localizacaoAtualizada.getPais());
        localizacao.setCoordenadas(localizacaoAtualizada.getCoordenadas());
        localizacao.setCep(localizacaoAtualizada.getCep());

        return repository.save(localizacao);
    }

    public void deletar(Long id) {
        Localizacao localizacao = buscarPorId(id);

        if (propriedadeRepository.existsByLocalizacaoId(id)) {
            throw new BusinessException(
                    "Não é possível deletar esta localização, pois ela está vinculada a uma propriedade."
            );
        }

        repository.delete(localizacao);
    }
}