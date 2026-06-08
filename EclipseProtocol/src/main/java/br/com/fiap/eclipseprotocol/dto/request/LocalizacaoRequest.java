package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;

public record LocalizacaoRequest(

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100)
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (ex: SP)")
        String estado,

        @NotBlank(message = "País é obrigatório")
        @Size(max = 60)
        String pais,

        Double latitude,
        Double longitude,

        @NotBlank(message = "CEP é obrigatório")
        @Size(max = 10)
        String cep
) {}