package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_LOCALIZACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOCALIZACAO")
    private Long id;

    @Column(name = "DS_CIDADE", nullable = false, length = 100)
    private String cidade;

    @Column(name = "DS_ESTADO", nullable = false, length = 2)
    private String estado;

    @Column(name = "DS_PAIS", nullable = false, length = 60)
    private String pais;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",  column = @Column(name = "NR_LATITUDE",  nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "NR_LONGITUDE", nullable = false))
    })
    private Coordenadas coordenadas;

    @Column(name = "DS_CEP", length = 10)
    private String cep;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordenadas {
        private Double latitude;
        private Double longitude;
    }
}
