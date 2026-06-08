package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Localizacao;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class LocalizacaoResponse extends RepresentationModel<LocalizacaoResponse> {

    private final Long id;
    private final String cidade;
    private final String estado;
    private final String pais;
    private final Double latitude;
    private final Double longitude;
    private final String cep;

    private LocalizacaoResponse(Long id, String cidade, String estado, String pais,
                                Double latitude, Double longitude, String cep) {
        this.id = id;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cep = cep;
    }

    public static LocalizacaoResponse from(Localizacao l) {
        return new LocalizacaoResponse(
                l.getId(),
                l.getCidade(),
                l.getEstado(),
                l.getPais(),
                l.getCoordenadas() != null ? l.getCoordenadas().getLatitude()  : null,
                l.getCoordenadas() != null ? l.getCoordenadas().getLongitude() : null,
                l.getCep()
        );
    }
}