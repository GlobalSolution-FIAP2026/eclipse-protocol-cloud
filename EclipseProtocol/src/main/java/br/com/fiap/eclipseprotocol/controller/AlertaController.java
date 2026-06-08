package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.AlertaRequest;
import br.com.fiap.eclipseprotocol.dto.response.AlertaResponse;
import br.com.fiap.eclipseprotocol.model.Alerta;
import br.com.fiap.eclipseprotocol.model.Leitura;
import br.com.fiap.eclipseprotocol.service.AlertaService;
import br.com.fiap.eclipseprotocol.service.LeituraService;
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

@Tag(name = "Alertas", description = "Gerenciamento dos alertas gerados")
@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaService alertaService;
    private final LeituraService leituraService;

    public AlertaController(AlertaService alertaService, LeituraService leituraService) {
        this.alertaService = alertaService;
        this.leituraService = leituraService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<AlertaResponse>> listarTodos() {
        List<AlertaResponse> lista = alertaService.listarTodos()
                .stream()
                .map(alerta -> {
                    AlertaResponse response = AlertaResponse.from(alerta);
                    response.add(linkTo(methodOn(AlertaController.class).buscarPorId(alerta.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<AlertaResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(AlertaController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<AlertaResponse> buscarPorId(@PathVariable Long id) {
        Alerta alerta = alertaService.buscarPorId(id);
        AlertaResponse response = AlertaResponse.from(alerta);
        response.add(linkTo(methodOn(AlertaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(AlertaController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(AlertaController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<AlertaResponse> criar(@RequestBody @Valid AlertaRequest request) {
        Leitura leitura = leituraService.buscarPorId(request.idLeitura());

        Alerta alerta = Alerta.builder()
                .tipoAlerta(Alerta.TipoAlerta.valueOf(request.tipoAlerta()))
                .severidade(Alerta.Severidade.valueOf(request.severidade()))
                .mensagem(request.mensagem())
                .status(Alerta.StatusAlerta.ABERTO)
                .dataCriacao(LocalDateTime.now())
                .leitura(leitura)
                .build();

        Alerta salvo = alertaService.salvar(alerta);
        AlertaResponse response = AlertaResponse.from(salvo);
        response.add(linkTo(methodOn(AlertaController.class).buscarPorId(salvo.getId())).withSelfRel());
        response.add(linkTo(methodOn(AlertaController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<AlertaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AlertaRequest request) {
        Leitura leitura = leituraService.buscarPorId(request.idLeitura());

        Alerta alertaAtualizado = Alerta.builder()
                .tipoAlerta(Alerta.TipoAlerta.valueOf(request.tipoAlerta()))
                .severidade(Alerta.Severidade.valueOf(request.severidade()))
                .mensagem(request.mensagem())
                .status(Alerta.StatusAlerta.ABERTO)
                .dataCriacao(LocalDateTime.now())
                .leitura(leitura)
                .build();

        Alerta atualizado = alertaService.atualizar(id, alertaAtualizado);
        AlertaResponse response = AlertaResponse.from(atualizado);
        response.add(linkTo(methodOn(AlertaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(AlertaController.class).listarTodos()).withRel("todos"));
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
        alertaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}