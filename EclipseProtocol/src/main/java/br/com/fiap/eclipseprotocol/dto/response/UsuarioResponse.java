package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Usuario;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Getter
public class UsuarioResponse extends RepresentationModel<UsuarioResponse> {

    private final Long id;
    private final String nome;
    private final String email;
    private final LocalDateTime dataCriacao;

    private UsuarioResponse(Long id, String nome, String email, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
    }

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getDataCriacao()
        );
    }
}