package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALERTA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERTA")
    private Long id;

    @Column(name = "DS_TIPO_ALERTA", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TipoAlerta tipoAlerta;

    @Column(name = "DS_SEVERIDADE", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Severidade severidade;

    @Column(name = "DS_MENSAGEM", nullable = false, length = 500)
    private String mensagem;

    @Column(name = "DS_STATUS", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private StatusAlerta status;

    @Column(name = "DT_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LEITURA")
    private Leitura leitura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PLANTACAO")
    private Plantacao plantacao;

    public enum TipoAlerta {
        TEMP_ALTA, TEMP_BAIXA,
        UMID_ALTA, UMID_BAIXA,
        NDVI_CRITICO, PRECIPITACAO_EXCESSIVA
    }

    public enum Severidade {
        BAIXA, MEDIA, ALTA, CRITICA
    }

    public enum StatusAlerta {
        ABERTO, RECONHECIDO, RESOLVIDO
    }
}