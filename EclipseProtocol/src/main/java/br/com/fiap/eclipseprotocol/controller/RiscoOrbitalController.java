package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.CreateRiscoOrbitalRequest;
import br.com.fiap.eclipseprotocol.dto.request.UpdateRiscoOrbitalRequest;
import br.com.fiap.eclipseprotocol.dto.response.RiscoOrbitalResponse;
import br.com.fiap.eclipseprotocol.model.LixoEspacial;
import br.com.fiap.eclipseprotocol.model.RiscoOrbital;
import br.com.fiap.eclipseprotocol.model.Satelite;
import br.com.fiap.eclipseprotocol.service.LixoEspacialService;
import br.com.fiap.eclipseprotocol.service.RiscoOrbitalService;
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

@Tag(name = "Riscos Orbitais", description = "Gerenciamento dos riscos orbitais identificados")
@RestController
@RequestMapping("/riscos-orbitais")
public class RiscoOrbitalController {

    private final RiscoOrbitalService riscoOrbitalService;
    private final SateliteService sateliteService;
    private final LixoEspacialService lixoEspacialService;

    public RiscoOrbitalController(RiscoOrbitalService riscoOrbitalService,
                                  SateliteService sateliteService,
                                  LixoEspacialService lixoEspacialService) {
        this.riscoOrbitalService = riscoOrbitalService;
        this.sateliteService = sateliteService;
        this.lixoEspacialService = lixoEspacialService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<RiscoOrbitalResponse>> listarTodos() {
        List<RiscoOrbitalResponse> lista = riscoOrbitalService.listarTodos()
                .stream()
                .map(risco -> {
                    RiscoOrbitalResponse response = RiscoOrbitalResponse.from(risco);
                    response.add(linkTo(methodOn(RiscoOrbitalController.class)
                            .buscarPorId(risco.getId().getIdSatelite(), risco.getId().getIdLixoEspacial())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<RiscoOrbitalResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(RiscoOrbitalController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{idSatelite}/{idLixoEspacial}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pela chave composta (satélite + lixo espacial)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<RiscoOrbitalResponse> buscarPorId(
            @PathVariable Long idSatelite,
            @PathVariable Long idLixoEspacial) {
        RiscoOrbital risco = riscoOrbitalService.buscarPorId(idSatelite, idLixoEspacial);
        RiscoOrbitalResponse response = RiscoOrbitalResponse.from(risco);
        response.add(linkTo(methodOn(RiscoOrbitalController.class).buscarPorId(idSatelite, idLixoEspacial)).withSelfRel());
        response.add(linkTo(methodOn(RiscoOrbitalController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(RiscoOrbitalController.class).deletar(idSatelite, idLixoEspacial)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<RiscoOrbitalResponse> criar(@RequestBody @Valid CreateRiscoOrbitalRequest request) {
        Satelite satelite = sateliteService.buscarPorId(request.idSatelite());
        LixoEspacial lixoEspacial = lixoEspacialService.buscarPorId(request.idLixoEspacial());

        RiscoOrbital risco = RiscoOrbital.builder()
                .id(new RiscoOrbital.RiscoOrbitalId(satelite.getId(), lixoEspacial.getId()))
                .satelite(satelite)
                .lixoEspacial(lixoEspacial)
                .nivelRisco(RiscoOrbital.NivelRisco.valueOf(request.nivelRisco()))
                .descricaoRisco(request.descricaoRisco())
                .dataAnalise(request.dataAnalise())
                .build();

        RiscoOrbital salvo = riscoOrbitalService.salvar(risco);
        RiscoOrbitalResponse response = RiscoOrbitalResponse.from(salvo);
        response.add(linkTo(methodOn(RiscoOrbitalController.class)
                .buscarPorId(salvo.getId().getIdSatelite(), salvo.getId().getIdLixoEspacial())).withSelfRel());
        response.add(linkTo(methodOn(RiscoOrbitalController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{idSatelite}/{idLixoEspacial}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<RiscoOrbitalResponse> atualizar(
            @PathVariable Long idSatelite,
            @PathVariable Long idLixoEspacial,
            @RequestBody @Valid UpdateRiscoOrbitalRequest request
    ) {
        RiscoOrbital riscoAtualizado = RiscoOrbital.builder()
                .nivelRisco(RiscoOrbital.NivelRisco.valueOf(request.nivelRisco()))
                .descricaoRisco(request.descricaoRisco())
                .dataAnalise(request.dataAnalise())
                .build();

        RiscoOrbital atualizado = riscoOrbitalService.atualizar(idSatelite, idLixoEspacial, riscoAtualizado);
        RiscoOrbitalResponse response = RiscoOrbitalResponse.from(atualizado);
        response.add(linkTo(methodOn(RiscoOrbitalController.class).buscarPorId(idSatelite, idLixoEspacial)).withSelfRel());
        response.add(linkTo(methodOn(RiscoOrbitalController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idSatelite}/{idLixoEspacial}")
    @Operation(summary = "Deletar registro", description = "Remove um registro pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Registro possui vínculo e não pode ser deletado")
    })
    public ResponseEntity<Void> deletar(
            @PathVariable Long idSatelite,
            @PathVariable Long idLixoEspacial) {
        riscoOrbitalService.deletar(idSatelite, idLixoEspacial);
        return ResponseEntity.noContent().build();
    }
}
