package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateLixoEspacialRequest(

        @NotBlank(message = "Nome do objeto é obrigatório")
        @Size(max = 100)
        String nomeObjeto,

        @NotBlank(message = "Tipo do objeto é obrigatório")
        @Size(max = 50)
        String tipoObjeto,

        @Positive(message = "Altitude deve ser positiva")
        Double altitudeKm,

        @Positive(message = "Velocidade deve ser positiva")
        Double velocidadeKmh,

        @Size(max = 50)
        String orbita,

        @NotNull(message = "Data de identificação é obrigatória")
        LocalDate dataIdentificacao
) {}

