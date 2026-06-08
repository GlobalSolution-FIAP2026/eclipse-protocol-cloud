package br.com.fiap.eclipseprotocol.dto.request;

import jakarta.validation.constraints.*;

public record PropriedadeRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String nome,

        @NotBlank(message = "Proprietário é obrigatório")
        @Size(max = 100)
        String proprietario,

        @NotNull(message = "Área total é obrigatória")
        @Positive(message = "Área total deve ser positiva")
        Double areaTotal,

        @Size(max = 50)
        String tipoSolo,

        @NotNull(message = "Localização é obrigatória")
        Long idLocalizacao,

        @NotNull(message = "Usuário é obrigatório")
        Long idUsuario
) {}