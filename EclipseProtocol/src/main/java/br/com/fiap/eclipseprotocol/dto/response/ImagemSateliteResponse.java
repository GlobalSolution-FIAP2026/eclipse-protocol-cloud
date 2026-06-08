package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.ImagemSatelite;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
public class ImagemSateliteResponse extends RepresentationModel<ImagemSateliteResponse> {

    private final Long id;
    private final Long idSatelite;
    private final String nomeSatelite;
    private final Long idPlantacao;
    private final String nomePlantacao;
    private final String urlImagem;
    private final Double ndvi;
    private final Double coberturaNuvem;
    private final LocalDateTime dataCaptura;

    private ImagemSateliteResponse(Long id, Long idSatelite, String nomeSatelite,
                                    Long idPlantacao, String nomePlantacao, String urlImagem,
                                    Double ndvi, Double coberturaNuvem, LocalDateTime dataCaptura) {
        this.id = id;
        this.idSatelite = idSatelite;
        this.nomeSatelite = nomeSatelite;
        this.idPlantacao = idPlantacao;
        this.nomePlantacao = nomePlantacao;
        this.urlImagem = urlImagem;
        this.ndvi = ndvi;
        this.coberturaNuvem = coberturaNuvem;
        this.dataCaptura = dataCaptura;
    }

    public static ImagemSateliteResponse from(ImagemSatelite i) {
        return new ImagemSateliteResponse(
                i.getId(),
                i.getSatelite() != null ? i.getSatelite().getId() : null,
                i.getSatelite() != null ? i.getSatelite().getNome() : null,
                i.getPlantacao() != null ? i.getPlantacao().getId() : null,
                i.getPlantacao() != null ? i.getPlantacao().getNome() : null,
                i.getUrlImagem(),
                i.getNdvi(),
                i.getCoberturaNuvem(),
                i.getDataCaptura()
        );
    }
}

