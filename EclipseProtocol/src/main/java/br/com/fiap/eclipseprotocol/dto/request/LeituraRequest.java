package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;

public record LeituraRequest(

        @NotNull(message = "Sensor é obrigatório")
        Long sensor,

        Double temperatura,
        Double umidade,
        Double precipitacao,
        Double ndvi
) {}