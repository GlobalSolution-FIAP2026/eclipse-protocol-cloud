package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LEITURA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEITURA")
    private Long id;

    @Column(name = "NR_TEMPERATURA")
    private Double temperatura;

    @Column(name = "NR_UMIDADE")
    private Double umidade;

    @Column(name = "NR_PRECIPITACAO")
    private Double precipitacao;

    @Column(name = "NR_NDVI")
    private Double ndvi;

    @Column(name = "DT_LEITURA", nullable = false)
    private LocalDateTime dataLeitura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SENSOR")
    private Sensor sensor;
}