package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.LeituraRequest;
import br.com.fiap.eclipseprotocol.dto.response.LeituraResponse;
import br.com.fiap.eclipseprotocol.model.Leitura;
import br.com.fiap.eclipseprotocol.model.Sensor;
import br.com.fiap.eclipseprotocol.service.LeituraService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Leituras", description = "Gerenciamento das leituras dos sensores")
@RestController
@RequestMapping("/leituras")
public class LeituraController {

    private final LeituraService leituraService;
    private final SensorService sensorService;

    public LeituraController(LeituraService leituraService, SensorService sensorService) {
        this.leituraService = leituraService;
        this.sensorService = sensorService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<LeituraResponse>> listarTodos() {
        List<LeituraResponse> lista = leituraService.listarTodos()
                .stream()
                .map(leitura -> {
                    LeituraResponse response = LeituraResponse.from(leitura);
                    response.add(linkTo(methodOn(LeituraController.class).buscarPorId(leitura.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<LeituraResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(LeituraController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<LeituraResponse> buscarPorId(@PathVariable Long id) {
        Leitura leitura = leituraService.buscarPorId(id);
        LeituraResponse response = LeituraResponse.from(leitura);
        response.add(linkTo(methodOn(LeituraController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(LeituraController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(LeituraController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<LeituraResponse> criar(@RequestBody @Valid LeituraRequest request) {
        Sensor sensor = sensorService.buscarPorId(request.sensor());

        Leitura leitura = Leitura.builder()
                .temperatura(request.temperatura())
                .umidade(request.umidade())
                .precipitacao(request.precipitacao())
                .ndvi(request.ndvi())
                .dataLeitura(LocalDateTime.now())
                .sensor(sensor)
                .build();

        Leitura salva = leituraService.salvar(leitura);
        LeituraResponse response = LeituraResponse.from(salva);
        response.add(linkTo(methodOn(LeituraController.class).buscarPorId(salva.getId())).withSelfRel());
        response.add(linkTo(methodOn(LeituraController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<LeituraResponse> atualizar(@PathVariable Long id, @RequestBody @Valid LeituraRequest request) {
        Sensor sensor = sensorService.buscarPorId(request.sensor());

        Leitura leituraAtualizada = Leitura.builder()
                .temperatura(request.temperatura())
                .umidade(request.umidade())
                .precipitacao(request.precipitacao())
                .ndvi(request.ndvi())
                .dataLeitura(LocalDateTime.now())
                .sensor(sensor)
                .build();

        Leitura atualizada = leituraService.atualizar(id, leituraAtualizada);
        LeituraResponse response = LeituraResponse.from(atualizada);
        response.add(linkTo(methodOn(LeituraController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(LeituraController.class).listarTodos()).withRel("todos"));
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
        leituraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}