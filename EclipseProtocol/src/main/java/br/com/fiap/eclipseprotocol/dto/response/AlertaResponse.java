package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Alerta;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
public class AlertaResponse extends RepresentationModel<AlertaResponse> {
    private Long id;
    private String tipoAlerta;
    private String severidade;
    private String mensagem;
    private String status;
    private LocalDateTime dataCriacao;
    private Long idLeitura;
    private Long idPlantacao;
    private String cultura;

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
                a.getLeitura() != null ? a.getLeitura().getId() : null,
                a.getLeitura() != null && a.getLeitura().getSensor() != null
                        && a.getLeitura().getSensor().getPlantacao() != null
                        ? a.getLeitura().getSensor().getPlantacao().getId() : null,
                a.getLeitura() != null && a.getLeitura().getSensor() != null
                        && a.getLeitura().getSensor().getPlantacao() != null
                        ? a.getLeitura().getSensor().getPlantacao().getCultura() : null
        );
    }
}
