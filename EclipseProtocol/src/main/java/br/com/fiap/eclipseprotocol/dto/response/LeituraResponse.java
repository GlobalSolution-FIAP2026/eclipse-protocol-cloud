package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Leitura;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Getter
public class LeituraResponse extends RepresentationModel<LeituraResponse> {

    private final Long id;
    private final Double temperatura;
    private final Double umidade;
    private final Double precipitacao;
    private final Double ndvi;
    private final LocalDateTime dataLeitura;
    private final Long sensor;

    private LeituraResponse(Long id, Double temperatura, Double umidade, Double precipitacao,
                            Double ndvi, LocalDateTime dataLeitura, Long sensor) {
        this.id = id;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.precipitacao = precipitacao;
        this.ndvi = ndvi;
        this.dataLeitura = dataLeitura;
        this.sensor = sensor;
    }

    public static LeituraResponse from(Leitura l) {
        return new LeituraResponse(
                l.getId(),
                l.getTemperatura(),
                l.getUmidade(),
                l.getPrecipitacao(),
                l.getNdvi(),
                l.getDataLeitura(),
                l.getSensor().getId()
        );
    }
}