package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record SensorRequest(

        @Size(max = 100)
        String nome,

        @NotBlank(message = "Tipo do sensor é obrigatório")
        String tipo,

        @NotNull(message = "Plantação é obrigatória")
        Long idPlantacao
) {}