package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;

public record AlertaRequest(

        @NotNull(message = "Leitura é obrigatória")
        Long idLeitura,

        @NotNull(message = "Cultura é obrigatória")
        Long idPlantacao,

        @NotBlank(message = "Tipo do alerta é obrigatório")
        String tipoAlerta,

        String severidade,

        @NotBlank(message = "Mensagem é obrigatória")
        @Size(max = 500)
        String mensagem
) {}