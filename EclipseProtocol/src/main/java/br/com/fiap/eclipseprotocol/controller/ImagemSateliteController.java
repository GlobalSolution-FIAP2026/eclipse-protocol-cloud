package br.com.fiap.eclipseprotocol.controller;

import br.com.fiap.eclipseprotocol.dto.request.CreateImagemSateliteRequest;
import br.com.fiap.eclipseprotocol.dto.request.UpdateImagemSateliteRequest;
import br.com.fiap.eclipseprotocol.dto.response.ImagemSateliteResponse;
import br.com.fiap.eclipseprotocol.model.ImagemSatelite;
import br.com.fiap.eclipseprotocol.model.Plantacao;
import br.com.fiap.eclipseprotocol.model.Satelite;
import br.com.fiap.eclipseprotocol.service.ImagemSateliteService;
import br.com.fiap.eclipseprotocol.service.PlantacaoService;
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

@Tag(name = "Imagens de Satélite", description = "Gerenciamento das imagens capturadas por satélites")
@RestController
@RequestMapping("/imagens-satelite")
public class ImagemSateliteController {

    private final ImagemSateliteService imagemSateliteService;
    private final SateliteService sateliteService;
    private final PlantacaoService plantacaoService;

    public ImagemSateliteController(ImagemSateliteService imagemSateliteService,
                                    SateliteService sateliteService,
                                    PlantacaoService plantacaoService) {
        this.imagemSateliteService = imagemSateliteService;
        this.sateliteService = sateliteService;
        this.plantacaoService = plantacaoService;
    }

    @GetMapping
    @Operation(summary = "Listar registros", description = "Retorna todos os registros cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum registro cadastrado")
    })
    public ResponseEntity<CollectionModel<ImagemSateliteResponse>> listarTodos() {
        List<ImagemSateliteResponse> lista = imagemSateliteService.listarTodos()
                .stream()
                .map(imagem -> {
                    ImagemSateliteResponse response = ImagemSateliteResponse.from(imagem);
                    response.add(linkTo(methodOn(ImagemSateliteController.class).buscarPorId(imagem.getId())).withSelfRel());
                    return response;
                })
                .toList();

        CollectionModel<ImagemSateliteResponse> collection = CollectionModel.of(lista,
                linkTo(methodOn(ImagemSateliteController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um registro específico pelo ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<ImagemSateliteResponse> buscarPorId(@PathVariable Long id) {
        ImagemSatelite imagem = imagemSateliteService.buscarPorId(id);
        ImagemSateliteResponse response = ImagemSateliteResponse.from(imagem);
        response.add(linkTo(methodOn(ImagemSateliteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ImagemSateliteController.class).listarTodos()).withRel("todos"));
        response.add(linkTo(methodOn(ImagemSateliteController.class).deletar(id)).withRel("deletar"));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar registro", description = "Cadastra um novo registro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    public ResponseEntity<ImagemSateliteResponse> criar(@RequestBody @Valid CreateImagemSateliteRequest request) {
        Satelite satelite = sateliteService.buscarPorId(request.idSatelite());
        Plantacao plantacao = plantacaoService.buscarPorId(request.idPlantacao());

        ImagemSatelite imagem = ImagemSatelite.builder()
                .satelite(satelite)
                .plantacao(plantacao)
                .urlImagem(request.urlImagem())
                .ndvi(request.ndvi())
                .coberturaNuvem(request.coberturaNuvem())
                .dataCaptura(request.dataCaptura())
                .build();

        ImagemSatelite salva = imagemSateliteService.salvar(imagem);
        ImagemSateliteResponse response = ImagemSateliteResponse.from(salva);
        response.add(linkTo(methodOn(ImagemSateliteController.class).buscarPorId(salva.getId())).withSelfRel());
        response.add(linkTo(methodOn(ImagemSateliteController.class).listarTodos()).withRel("todos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro", description = "Atualiza um registro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<ImagemSateliteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UpdateImagemSateliteRequest request
    ) {
        Satelite satelite = sateliteService.buscarPorId(request.idSatelite());
        Plantacao plantacao = plantacaoService.buscarPorId(request.idPlantacao());

        ImagemSatelite imagemAtualizada = ImagemSatelite.builder()
                .satelite(satelite)
                .plantacao(plantacao)
                .urlImagem(request.urlImagem())
                .ndvi(request.ndvi())
                .coberturaNuvem(request.coberturaNuvem())
                .dataCaptura(request.dataCaptura())
                .build();

        ImagemSatelite atualizada = imagemSateliteService.atualizar(id, imagemAtualizada);
        ImagemSateliteResponse response = ImagemSateliteResponse.from(atualizada);
        response.add(linkTo(methodOn(ImagemSateliteController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ImagemSateliteController.class).listarTodos()).withRel("todos"));
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
        imagemSateliteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

