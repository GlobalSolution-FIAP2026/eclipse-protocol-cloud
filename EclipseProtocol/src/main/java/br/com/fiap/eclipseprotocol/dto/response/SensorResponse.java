package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Sensor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class SensorResponse extends RepresentationModel<SensorResponse> {

    private final Long id;
    private final String nome;
    private final String tipo;
    private final Long plantacao;

    private SensorResponse(Long id, String nome, String tipo, Long plantacao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.plantacao = plantacao;
    }

    public static SensorResponse from(Sensor s) {
        return new SensorResponse(
                s.getId(),
                s.getNome(),
                s.getTipo(),
                s.getPlantacao().getId()
        );
    }
}