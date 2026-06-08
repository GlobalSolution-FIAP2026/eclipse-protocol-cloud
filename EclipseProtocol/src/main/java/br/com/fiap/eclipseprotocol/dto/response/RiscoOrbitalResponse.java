package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.RiscoOrbital;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
public class RiscoOrbitalResponse extends RepresentationModel<RiscoOrbitalResponse> {

    private final Long id;
    private final Long idSatelite;
    private final String nomeSatelite;
    private final Long idLixoEspacial;
    private final String nomeObjeto;
    private final String nivelRisco;
    private final String descricaoRisco;
    private final LocalDateTime dataAnalise;

    private RiscoOrbitalResponse(Long id, Long idSatelite, String nomeSatelite,
                                  Long idLixoEspacial, String nomeObjeto,
                                  String nivelRisco, String descricaoRisco,
                                  LocalDateTime dataAnalise) {
        this.id = id;
        this.idSatelite = idSatelite;
        this.nomeSatelite = nomeSatelite;
        this.idLixoEspacial = idLixoEspacial;
        this.nomeObjeto = nomeObjeto;
        this.nivelRisco = nivelRisco;
        this.descricaoRisco = descricaoRisco;
        this.dataAnalise = dataAnalise;
    }

    public static RiscoOrbitalResponse from(RiscoOrbital r) {
        return new RiscoOrbitalResponse(
                null,
                r.getId() != null ? r.getId().getIdSatelite() : null,
                r.getSatelite() != null ? r.getSatelite().getNome() : null,
                r.getId() != null ? r.getId().getIdLixoEspacial() : null,
                r.getLixoEspacial() != null ? r.getLixoEspacial().getNomeObjeto() : null,
                r.getNivelRisco() != null ? r.getNivelRisco().name() : null,
                r.getDescricaoRisco(),
                r.getDataAnalise()
        );
    }
}

