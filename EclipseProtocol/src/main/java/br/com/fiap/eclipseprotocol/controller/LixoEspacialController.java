package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.CreateLixoEspacialRequest;
import br.com.fiap.eclipseprotocol.dto.request.UpdateLixoEspacialRequest;
import br.com.fiap.eclipseprotocol.dto.response.LixoEspacialResponse;
import br.com.fiap.eclipseprotocol.model.LixoEspacial;
import br.com.fiap.eclipseprotocol.service.LixoEspacialService;
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

@Tag(name = "Lixo Espacial", description = "Gerenciamento dos objetos de lixo espacial rastreados")
@RestController
@RequestMapping("/lixo-espacial")
public class LixoEspacialController {

    private final LixoEspacialService lixoEspacialService;

    public LixoEspacialController(LixoEspacialService lixoEspacialService) {
        this.lixoEspacialService = lixoEspacialService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<LixoEspacialResponse>> listarTodos() {
        List<LixoEspacialResponse> lista = lixoEspacialService.listarTodos()
                .stream()
                .map(lixo -> {
                    LixoEspacialResponse response = LixoEspacialResponse.from(lixo);
                    response.add(linkTo(methodOn(LixoEspacialController.class).buscarPorId(lixo.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<LixoEspacialResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(LixoEspacialController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<LixoEspacialResponse> buscarPorId(@PathVariable Long id) {
        LixoEspacial lixo = lixoEspacialService.buscarPorId(id);
        LixoEspacialResponse response = LixoEspacialResponse.from(lixo);
        response.add(linkTo(methodOn(LixoEspacialController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(LixoEspacialController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(LixoEspacialController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<LixoEspacialResponse> criar(@RequestBody @Valid CreateLixoEspacialRequest request) {
        LixoEspacial lixo = LixoEspacial.builder()
                .nomeObjeto(request.nomeObjeto())
                .tipoObjeto(request.tipoObjeto())
                .altitudeKm(request.altitudeKm())
                .velocidadeKmh(request.velocidadeKmh())
                .orbita(request.orbita())
                .dataIdentificacao(request.dataIdentificacao())
                .build();

        LixoEspacial salvo = lixoEspacialService.salvar(lixo);
        LixoEspacialResponse response = LixoEspacialResponse.from(salvo);
        response.add(linkTo(methodOn(LixoEspacialController.class).buscarPorId(salvo.getId())).withSelfRel());
        response.add(linkTo(methodOn(LixoEspacialController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<LixoEspacialResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLixoEspacialRequest request
    ) {
        LixoEspacial lixoAtualizado = LixoEspacial.builder()
                .nomeObjeto(request.nomeObjeto())
                .tipoObjeto(request.tipoObjeto())
                .altitudeKm(request.altitudeKm())
                .velocidadeKmh(request.velocidadeKmh())
                .orbita(request.orbita())
                .dataIdentificacao(request.dataIdentificacao())
                .build();

        LixoEspacial atualizado = lixoEspacialService.atualizar(id, lixoAtualizado);
        LixoEspacialResponse response = LixoEspacialResponse.from(atualizado);
        response.add(linkTo(methodOn(LixoEspacialController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(LixoEspacialController.class).listarTodos()).withRel("todos"));
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
        lixoEspacialService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

