package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateSateliteRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotBlank(message = "Tipo é obrigatório")
        @Size(max = 50)
        String tipo,

        @Size(max = 50)
        String orbita,

        @Positive(message = "Altitude deve ser positiva")
        Double altitudeKm,

        @NotBlank(message = "Status é obrigatório")
        String status,

        LocalDate dataLancamento
) {}

