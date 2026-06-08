package br.com.fiap.eclipseprotocol.service;

import br.com.fiap.eclipseprotocol.exception.ResourceNotFoundException;
import br.com.fiap.eclipseprotocol.model.ImagemSatelite;
import br.com.fiap.eclipseprotocol.repository.ImagemSateliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagemSateliteService {

    private final ImagemSateliteRepository repository;

    public ImagemSateliteService(ImagemSateliteRepository repository) {
        this.repository = repository;
    }

    public List<ImagemSatelite> listarTodos() {
        return repository.findAll();
    }

    public ImagemSatelite buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagem de satélite não encontrada"));
    }

    public ImagemSatelite salvar(ImagemSatelite imagemSatelite) {
        return repository.save(imagemSatelite);
    }

    public ImagemSatelite atualizar(Long id, ImagemSatelite imagemAtualizada) {
        ImagemSatelite imagem = buscarPorId(id);

        imagem.setSatelite(imagemAtualizada.getSatelite());
        imagem.setPlantacao(imagemAtualizada.getPlantacao());
        imagem.setUrlImagem(imagemAtualizada.getUrlImagem());
        imagem.setNdvi(imagemAtualizada.getNdvi());
        imagem.setCoberturaNuvem(imagemAtualizada.getCoberturaNuvem());
        imagem.setDataCaptura(imagemAtualizada.getDataCaptura());

        return repository.save(imagem);
    }

    public void deletar(Long id) {
        ImagemSatelite imagem = buscarPorId(id);
        repository.delete(imagem);
    }
}

