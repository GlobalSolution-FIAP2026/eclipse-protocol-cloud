package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record UpdateRiscoOrbitalRequest(

        @NotNull(message = "Satélite é obrigatório")
        Long idSatelite,

        @NotNull(message = "Lixo espacial é obrigatório")
        Long idLixoEspacial,

        @NotBlank(message = "Nível de risco é obrigatório")
        String nivelRisco,

        @Size(max = 500)
        String descricaoRisco,

        @NotNull(message = "Data de análise é obrigatória")
        LocalDateTime dataAnalise
) {}

