package br.com.fiap.eclipseprotocol.dto.response;

import br.com.fiap.eclipseprotocol.model.Propriedade;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class PropriedadeResponse extends RepresentationModel<PropriedadeResponse> {

    private final Long id;
    private final String nome;
    private final String proprietario;
    private final Double areaTotal;
    private final String tipoSolo;
    private final LocalizacaoResponse localizacao;
    private final UsuarioResponse usuario;

    private PropriedadeResponse(Long id, String nome, String proprietario, Double areaTotal,
                                String tipoSolo, LocalizacaoResponse localizacao, UsuarioResponse usuario) {
        this.id = id;
        this.nome = nome;
        this.proprietario = proprietario;
        this.areaTotal = areaTotal;
        this.tipoSolo = tipoSolo;
        this.localizacao = localizacao;
        this.usuario = usuario;
    }

    public static PropriedadeResponse from(Propriedade p) {
        return new PropriedadeResponse(
                p.getId(),
                p.getNome(),
                p.getProprietario(),
                p.getAreaTotal(),
                p.getTipoSolo(),
                LocalizacaoResponse.from(p.getLocalizacao()),
                UsuarioResponse.from(p.getUsuario())
        );
    }
}