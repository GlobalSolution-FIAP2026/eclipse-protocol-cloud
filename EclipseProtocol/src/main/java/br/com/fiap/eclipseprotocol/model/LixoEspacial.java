package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_LIXO_ESPACIAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LixoEspacial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIXO_ESPACIAL")
    private Long id;

    @Column(name = "NM_OBJETO", nullable = false, length = 100)
    private String nomeObjeto;

    @Column(name = "TP_OBJETO", nullable = false, length = 50)
    private String tipoObjeto;

    @Column(name = "NR_ALTITUDE_KM")
    private Double altitudeKm;

    @Column(name = "NR_VELOCIDADE_KMH")
    private Double velocidadeKmh;

    @Column(name = "DS_ORBITA", length = 50)
    private String orbita;

    @Column(name = "DT_IDENTIFICACAO", nullable = false)
    private LocalDate dataIdentificacao;
}

