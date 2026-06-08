package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;

public record PlantacaoRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotBlank(message = "Cultura é obrigatória")
        @Size(max = 80)
        String cultura,

        @NotNull(message = "Área é obrigatória")
        @Positive(message = "Área deve ser positiva")
        Double areaHectares,

        @NotBlank(message = "Status é obrigatório")
        String status,

        @NotNull(message = "Propriedade é obrigatória")
        Long idPropriedade
) {
}