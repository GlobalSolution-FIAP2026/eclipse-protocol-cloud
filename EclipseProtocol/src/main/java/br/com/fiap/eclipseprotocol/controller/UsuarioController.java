package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.UsuarioRequest;
import br.com.fiap.eclipseprotocol.dto.response.UsuarioResponse;
import br.com.fiap.eclipseprotocol.model.Usuario;
import br.com.fiap.eclipseprotocol.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Usuários", description = "Gerenciamento de usuários da aplicação")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> lista = service.listarTodos()
                .stream()
                .map(usuario -> {
                    UsuarioResponse response = UsuarioResponse.from(usuario);
                    response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(usuario.getId())).withSelfRel());
                    return response;
                })
                .toList();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CollectionModel<UsuarioResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(UsuarioController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        UsuarioResponse response = UsuarioResponse.from(usuario);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(UsuarioController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .build();

        Usuario salvo = service.salvar(usuario);
        UsuarioResponse response = UsuarioResponse.from(salvo);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(salvo.getId())).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        Usuario usuarioAtualizado = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .build();

        Usuario atualizado = service.atualizar(id, usuarioAtualizado);
        UsuarioResponse response = UsuarioResponse.from(atualizado);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro", description = "Remove um registro pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Registro possui vínculo e não pode ser deletado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}