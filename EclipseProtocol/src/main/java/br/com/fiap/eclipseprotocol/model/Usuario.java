package br.com.fiap.eclipseprotocol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_USUARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NM_USUARIO", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @Column(name = "FL_ATIVO", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "DT_CRIACAO", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Propriedade> propriedades;
}
