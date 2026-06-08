package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_SATELITE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SATELITE")
    private Long id;

    @Column(name = "NM_SATELITE", nullable = false, length = 100)
    private String nome;

    @Column(name = "TP_SATELITE", nullable = false, length = 50)
    private String tipo;

    @Column(name = "DS_ORBITA", length = 50)
    private String orbita;

    @Column(name = "NR_ALTITUDE_KM")
    private Double altitudeKm;

    @Column(name = "DS_STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusSatelite status;

    @Column(name = "DT_LANCAMENTO")
    private LocalDate dataLancamento;

    public enum StatusSatelite {
        ATIVO, INATIVO, DESCOMISSIONADO
    }
}

