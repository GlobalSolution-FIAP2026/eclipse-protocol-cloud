package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.PlantacaoRequest;
import br.com.fiap.eclipseprotocol.dto.response.PlantacaoResponse;
import br.com.fiap.eclipseprotocol.model.Plantacao;
import br.com.fiap.eclipseprotocol.model.Propriedade;
import br.com.fiap.eclipseprotocol.service.PlantacaoService;
import br.com.fiap.eclipseprotocol.service.PropriedadeService;
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

@Tag(name = "Plantações", description = "Gerenciamento das plantações monitoradas")
@RestController
@RequestMapping("/plantacoes")
public class PlantacaoController {

    private final PlantacaoService plantacaoService;
    private final PropriedadeService propriedadeService;

    public PlantacaoController(PlantacaoService plantacaoService, PropriedadeService propriedadeService) {
        this.plantacaoService = plantacaoService;
        this.propriedadeService = propriedadeService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<PlantacaoResponse>> listarTodos() {
        List<PlantacaoResponse> lista = plantacaoService.listarTodos()
                .stream()
                .map(plantacao -> {
                    PlantacaoResponse response = PlantacaoResponse.from(plantacao);
                    response.add(linkTo(methodOn(PlantacaoController.class).buscarPorId(plantacao.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<PlantacaoResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(PlantacaoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<PlantacaoResponse> buscarPorId(@PathVariable Long id) {
        Plantacao plantacao = plantacaoService.buscarPorId(id);
        PlantacaoResponse response = PlantacaoResponse.from(plantacao);
        response.add(linkTo(methodOn(PlantacaoController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(PlantacaoController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(PlantacaoController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<PlantacaoResponse> criar(@RequestBody @Valid PlantacaoRequest request) {
        Propriedade propriedade = propriedadeService.buscarPorId(request.idPropriedade());

        Plantacao plantacao = Plantacao.builder()
                .nome(request.nome())
                .cultura(request.cultura())
                .areaHectares(request.areaHectares())
                .status(request.status())
                .propriedade(propriedade)
                .build();

        Plantacao salva = plantacaoService.salvar(plantacao);
        PlantacaoResponse response = PlantacaoResponse.from(salva);
        response.add(linkTo(methodOn(PlantacaoController.class).buscarPorId(salva.getId())).withSelfRel());
        response.add(linkTo(methodOn(PlantacaoController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<PlantacaoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid PlantacaoRequest request) {
        Propriedade propriedade = propriedadeService.buscarPorId(request.idPropriedade());

        Plantacao plantacaoAtualizada = Plantacao.builder()
                .nome(request.nome())
                .cultura(request.cultura())
                .areaHectares(request.areaHectares())
                .status(request.status())
                .propriedade(propriedade)
                .build();

        Plantacao atualizada = plantacaoService.atualizar(id, plantacaoAtualizada);
        PlantacaoResponse response = PlantacaoResponse.from(atualizada);
        response.add(linkTo(methodOn(PlantacaoController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(PlantacaoController.class).listarTodos()).withRel("todos"));
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
        plantacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}