package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Alerta;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
public class AlertaResponse extends RepresentationModel<AlertaResponse> {
    private final Long id;
    private final String tipoAlerta;
    private final String severidade;
    private final String mensagem;
    private final String status;
    private final LocalDateTime dataCriacao;
    private final Long idLeitura;
    private final Long idPlantacao;
    private final String cultura;

    private AlertaResponse(Long id, String tipoAlerta, String severidade, String mensagem,
                           String status, LocalDateTime dataCriacao, Long idLeitura,
                           Long idPlantacao, String cultura) {
        this.id = id;
        this.tipoAlerta = tipoAlerta;
        this.severidade = severidade;
        this.mensagem = mensagem;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.idLeitura = idLeitura;
        this.idPlantacao = idPlantacao;
        this.cultura = cultura;
    }

    public static AlertaResponse from(Alerta a) {
        return new AlertaResponse(
                a.getId(),
                a.getTipoAlerta() != null ? a.getTipoAlerta().name() : null,
                a.getSeveridade() != null ? a.getSeveridade().name() : null,
                a.getMensagem(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getDataCriacao(),
                // idLeitura — acessa só o ID, sem navegar nas relações lazy
                a.getLeitura() != null ? a.getLeitura().getId() : null,
                // idPlantacao — usa o campo direto de Alerta, sem passar por Leitura→Sensor
                a.getPlantacao() != null ? a.getPlantacao().getId() : null,
                // cultura — usa o campo direto de Alerta→Plantacao
                a.getPlantacao() != null ? a.getPlantacao().getCultura() : null
        );
    }
}
