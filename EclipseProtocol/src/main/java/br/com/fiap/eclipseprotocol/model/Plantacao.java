package br.com.fiap.eclipseprotocol.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_PLANTACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plantacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLANTACAO")
    private Long id;

    @Column(name = "NM_PLANTACAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "TP_CULTURA", nullable = false, length = 80)
    private String cultura;

    @Column(name = "NR_AREA_HECTARES", nullable = false)
    private Double areaHectares;

    @Column(name = "DS_STATUS", nullable = false, length = 30)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_PROPRIEDADE",
            nullable = false,
        foreignKey = @ForeignKey(name = "FK_PLANTACAO_PROPRIEDADE")
    )
    private Propriedade propriedade;

}
