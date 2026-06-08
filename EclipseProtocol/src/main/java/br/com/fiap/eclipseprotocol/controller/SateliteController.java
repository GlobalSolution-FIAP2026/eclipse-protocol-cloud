package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.CreateSateliteRequest;
import br.com.fiap.eclipseprotocol.dto.request.UpdateSateliteRequest;
import br.com.fiap.eclipseprotocol.dto.response.SateliteResponse;
import br.com.fiap.eclipseprotocol.model.Satelite;
import br.com.fiap.eclipseprotocol.service.SateliteService;
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

@Tag(name = "Satélites", description = "Gerenciamento dos satélites monitorados")
@RestController
@RequestMapping("/satelites")
public class SateliteController {

    private final SateliteService sateliteService;

    public SateliteController(SateliteService sateliteService) {
        this.sateliteService = sateliteService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<SateliteResponse>> listarTodos() {
        List<SateliteResponse> lista = sateliteService.listarTodos()
                .stream()
                .map(satelite -> {
                    SateliteResponse response = SateliteResponse.from(satelite);
                    response.add(linkTo(methodOn(SateliteController.class).buscarPorId(satelite.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<SateliteResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(SateliteController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<SateliteResponse> buscarPorId(@PathVariable Long id) {
        Satelite satelite = sateliteService.buscarPorId(id);
        SateliteResponse response = SateliteResponse.from(satelite);
        response.add(linkTo(methodOn(SateliteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(SateliteController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<SateliteResponse> criar(@RequestBody @Valid CreateSateliteRequest request) {
        Satelite satelite = Satelite.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .orbita(request.orbita())
                .altitudeKm(request.altitudeKm())
                .status(Satelite.StatusSatelite.valueOf(request.status()))
                .dataLancamento(request.dataLancamento())
                .build();

        Satelite salvo = sateliteService.salvar(satelite);
        SateliteResponse response = SateliteResponse.from(salvo);
        response.add(linkTo(methodOn(SateliteController.class).buscarPorId(salvo.getId())).withSelfRel());
        response.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<SateliteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UpdateSateliteRequest request
    ) {
        Satelite sateliteAtualizado = Satelite.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .orbita(request.orbita())
                .altitudeKm(request.altitudeKm())
                .status(Satelite.StatusSatelite.valueOf(request.status()))
                .dataLancamento(request.dataLancamento())
                .build();

        Satelite atualizado = sateliteService.atualizar(id, sateliteAtualizado);
        SateliteResponse response = SateliteResponse.from(atualizado);
        response.add(linkTo(methodOn(SateliteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos"));
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
        sateliteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
