package br.com.fiap.eclipseprotocol.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_SENSOR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DS_SUBTIPO", discriminatorType = DiscriminatorType.STRING, length = 50)
@DiscriminatorValue("GENERICO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SENSOR")
    private Long id;

    @Column(name = "NM_SENSOR", nullable = false, length = 100)
    private String nome;

    @Column(name = "TP_SENSOR", nullable = false, length = 50)
    private String tipo;

    @Column(name = "FL_ATIVO", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PLANTACAO")
    private Plantacao plantacao;

    @Entity
    @DiscriminatorValue("ESPECIALIZADO")
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SensorEspecializado extends Sensor {

        @Column(name = "DS_UNIDADE_MEDIDA", length = 20)
        private String unidadeMedida;
    }
}
