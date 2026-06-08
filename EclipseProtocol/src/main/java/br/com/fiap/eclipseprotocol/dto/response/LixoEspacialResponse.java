package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.LixoEspacial;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
public class LixoEspacialResponse extends RepresentationModel<LixoEspacialResponse> {

    private final Long id;
    private final String nomeObjeto;
    private final String tipoObjeto;
    private final Double altitudeKm;
    private final Double velocidadeKmh;
    private final String orbita;
    private final LocalDate dataIdentificacao;

    private LixoEspacialResponse(Long id, String nomeObjeto, String tipoObjeto,
                                  Double altitudeKm, Double velocidadeKmh,
                                  String orbita, LocalDate dataIdentificacao) {
        this.id = id;
        this.nomeObjeto = nomeObjeto;
        this.tipoObjeto = tipoObjeto;
        this.altitudeKm = altitudeKm;
        this.velocidadeKmh = velocidadeKmh;
        this.orbita = orbita;
        this.dataIdentificacao = dataIdentificacao;
    }

    public static LixoEspacialResponse from(LixoEspacial l) {
        return new LixoEspacialResponse(
                l.getId(),
                l.getNomeObjeto(),
                l.getTipoObjeto(),
                l.getAltitudeKm(),
                l.getVelocidadeKmh(),
                l.getOrbita(),
                l.getDataIdentificacao()
        );
    }
}

