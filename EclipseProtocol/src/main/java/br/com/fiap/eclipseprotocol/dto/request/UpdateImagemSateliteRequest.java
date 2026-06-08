package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record UpdateImagemSateliteRequest(

        @NotNull(message = "Satélite é obrigatório")
        Long idSatelite,

        @NotNull(message = "Plantação é obrigatória")
        Long idPlantacao,

        @NotBlank(message = "URL da imagem é obrigatória")
        @Size(max = 500)
        String urlImagem,

        Double ndvi,

        Double coberturaNuvem,

        @NotNull(message = "Data de captura é obrigatória")
        LocalDateTime dataCaptura
) {}

