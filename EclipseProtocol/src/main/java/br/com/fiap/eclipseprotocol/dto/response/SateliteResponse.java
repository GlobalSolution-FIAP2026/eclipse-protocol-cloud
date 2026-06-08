package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Satelite;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
public class SateliteResponse extends RepresentationModel<SateliteResponse> {

    private final Long id;
    private final String nome;
    private final String tipo;
    private final String orbita;
    private final Double altitudeKm;
    private final String status;
    private final LocalDate dataLancamento;

    private SateliteResponse(Long id, String nome, String tipo, String orbita,
                              Double altitudeKm, String status, LocalDate dataLancamento) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.orbita = orbita;
        this.altitudeKm = altitudeKm;
        this.status = status;
        this.dataLancamento = dataLancamento;
    }

    public static SateliteResponse from(Satelite s) {
        return new SateliteResponse(
                s.getId(),
                s.getNome(),
                s.getTipo(),
                s.getOrbita(),
                s.getAltitudeKm(),
                s.getStatus() != null ? s.getStatus().name() : null,
                s.getDataLancamento()
        );
    }
}

