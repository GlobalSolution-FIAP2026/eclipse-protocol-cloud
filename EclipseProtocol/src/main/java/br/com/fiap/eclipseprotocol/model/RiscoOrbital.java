package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_RISCO_ORBITAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiscoOrbital {

    @EmbeddedId
    private RiscoOrbitalId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSatelite")
    @JoinColumn(
            name = "ID_SATELITE",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_RISCO_SATELITE")
    )
    private Satelite satelite;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idLixoEspacial")
    @JoinColumn(
            name = "ID_LIXO_ESPACIAL",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_RISCO_LIXO_ESPACIAL")
    )
    private LixoEspacial lixoEspacial;

    @Column(name = "DS_NIVEL_RISCO", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NivelRisco nivelRisco;

    @Column(name = "DS_DESCRICAO_RISCO", length = 500)
    private String descricaoRisco;

    @Column(name = "DT_ANALISE", nullable = false)
    private LocalDateTime dataAnalise;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RiscoOrbitalId implements Serializable {

        @Column(name = "ID_SATELITE")
        private Long idSatelite;

        @Column(name = "ID_LIXO_ESPACIAL")
        private Long idLixoEspacial;
    }

    public enum NivelRisco {
        BAIXO, MODERADO, ALTO, CRITICO
    }
}

