package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Plantacao;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class PlantacaoResponse extends RepresentationModel<PlantacaoResponse> {

    private final Long id;
    private final String nome;
    private final String cultura;
    private final Double areaHectares;
    private final String status;
    private final Long idPropriedade;

    private PlantacaoResponse(Long id, String nome, String cultura, Double areaHectares,
                              String status, Long idPropriedade) {
        this.id = id;
        this.nome = nome;
        this.cultura = cultura;
        this.areaHectares = areaHectares;
        this.status = status;
        this.idPropriedade = idPropriedade;
    }

    public static PlantacaoResponse from(Plantacao p) {
        return new PlantacaoResponse(
                p.getId(),
                p.getNome(),
                p.getCultura(),
                p.getAreaHectares(),
                p.getStatus(),
                p.getPropriedade() != null ? p.getPropriedade().getId() : null
        );
    }
}