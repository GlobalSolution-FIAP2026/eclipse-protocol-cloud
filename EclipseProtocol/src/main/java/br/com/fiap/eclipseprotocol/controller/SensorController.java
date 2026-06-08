package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.SensorRequest;
import br.com.fiap.eclipseprotocol.dto.response.SensorResponse;
import br.com.fiap.eclipseprotocol.model.Plantacao;
import br.com.fiap.eclipseprotocol.model.Sensor;
import br.com.fiap.eclipseprotocol.service.PlantacaoService;
import br.com.fiap.eclipseprotocol.service.SensorService;
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

@Tag(name = "Sensores", description = "Gerenciamento dos sensores IoT")
@RestController
@RequestMapping("/sensores")
public class SensorController {

    private final SensorService sensorService;
    private final PlantacaoService plantacaoService;

    public SensorController(SensorService sensorService, PlantacaoService plantacaoService) {
        this.sensorService = sensorService;
        this.plantacaoService = plantacaoService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<SensorResponse>> listarTodos() {
        List<SensorResponse> lista = sensorService.listarTodos()
                .stream()
                .map(sensor -> {
                    SensorResponse response = SensorResponse.from(sensor);
                    response.add(linkTo(methodOn(SensorController.class).buscarPorId(sensor.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<SensorResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(SensorController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<SensorResponse> buscarPorId(@PathVariable Long id) {
        Sensor sensor = sensorService.buscarPorId(id);
        SensorResponse response = SensorResponse.from(sensor);
        response.add(linkTo(methodOn(SensorController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(SensorController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(SensorController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<SensorResponse> criar(@RequestBody @Valid SensorRequest request) {
        Plantacao plantacao = plantacaoService.buscarPorId(request.idPlantacao());

        Sensor sensor = Sensor.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .plantacao(plantacao)
                .build();

        Sensor salvo = sensorService.salvar(sensor);
        SensorResponse response = SensorResponse.from(salvo);
        response.add(linkTo(methodOn(SensorController.class).buscarPorId(salvo.getId())).withSelfRel());
        response.add(linkTo(methodOn(SensorController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<SensorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid SensorRequest request) {
        Plantacao plantacao = plantacaoService.buscarPorId(request.idPlantacao());

        Sensor sensorAtualizado = Sensor.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .plantacao(plantacao)
                .build();

        Sensor atualizado = sensorService.atualizar(id, sensorAtualizado);
        SensorResponse response = SensorResponse.from(atualizado);
        response.add(linkTo(methodOn(SensorController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(SensorController.class).listarTodos()).withRel("todos"));
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
        sensorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}