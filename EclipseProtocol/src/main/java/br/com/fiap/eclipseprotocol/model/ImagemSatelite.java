package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_IMAGEM_SATELITE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagemSatelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_IMAGEM")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_SATELITE",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_IMAGEM_SATELITE")
    )
    private Satelite satelite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_PLANTACAO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_IMAGEM_PLANTACAO")
    )
    private Plantacao plantacao;

    @Column(name = "URL_IMAGEM", nullable = false, length = 500)
    private String urlImagem;

    @Column(name = "NR_NDVI")
    private Double ndvi;

    @Column(name = "NR_COBERTURA_NUVEM")
    private Double coberturaNuvem;

    @Column(name = "DT_CAPTURA", nullable = false)
    private LocalDateTime dataCaptura;
}

