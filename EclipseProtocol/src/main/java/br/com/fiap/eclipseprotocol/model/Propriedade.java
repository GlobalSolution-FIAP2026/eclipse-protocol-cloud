package br.com.fiap.eclipseprotocol.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_PROPRIEDADE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROPRIEDADE")
    private Long id;

    @Column(name = "NM_PROPRIEDADE", nullable = false, length = 100)
    private String nome;

    @Column(name = "NM_PROPRIETARIO", nullable = false, length = 100)
    private String proprietario;

    @Column(name = "NR_AREA_TOTAL", nullable = false)
    private Double areaTotal;

    @Column(name = "DS_TIPO_SOLO", length = 50)
    private String tipoSolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_LOCALIZACAO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PROPRIEDADE_LOCALIZACAO")
    )
    private Localizacao localizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_USUARIO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_PROPRIEDADE_USUARIO")
    )
    private Usuario usuario;
}
