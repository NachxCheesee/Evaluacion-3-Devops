package cl.marfuego.ms_locales.controller;

import cl.marfuego.ms_locales.dto.ErrorDto;
import cl.marfuego.ms_locales.dto.LocalDto;
import cl.marfuego.ms_locales.dto.MesaDto;
import cl.marfuego.ms_locales.enums.EstadoMesa;
import cl.marfuego.ms_locales.model.Local;
import cl.marfuego.ms_locales.model.Mesa;
import cl.marfuego.ms_locales.service.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locales")
@Tag(name = "Locales", description = "Operaciones relacionadas con la gestión de Locales de MarFuego")
public class LocalController {

    private static final Logger log = LoggerFactory.getLogger(LocalController.class);
    private final LocalService localService;

    public LocalController(LocalService localService) {

        this.localService = localService;
    }

    @GetMapping
    @Operation(summary = "Obtener lista de locales", description = "Busca y agrupa todos los locales existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta de locales exitosa"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error inesperado en el servidor al procesar la lista de locales",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500ListarLocales",
                                    value = "{\"status\": 500, \"mensaje\": \"Error inesperado en el servidor al procesar la lista de locales\", \"timestamp\": \"2026-06-17T15:30:00\"}"
                            ) // <-- Forzado el 500 aquí
                    )
            )
    })
    public ResponseEntity<List<Local>> listarLocales() {
        log.info("--> [GET] Solicitud para listar todos los locales recibida.");
        List<Local> locales = localService.listarLocales();
        log.info("<-- [GET] Retornando lista con {} locales.", locales.size());
        return ResponseEntity.ok(locales);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener local por ID", description = "Busca un local específico utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Local encontrado con éxito"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID ingresado en la URL es inválido (debe ser un número)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400IdInvalido",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID ingresado en la URL es inválido (debe ser un número)\", \"timestamp\": \"2026-06-17T15:35:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ningún local con el ID ingresado en el sistema",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error404LocalNoEncontrado",
                                    value = "{\"status\": 404, \"mensaje\": \"No se encontró ningún local con el ID ingresado en el sistema\", \"timestamp\": \"2026-06-17T15:35:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor de MarFuego",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500ServidorLocal",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor de MarFuego\", \"timestamp\": \"2026-06-17T15:35:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Local> obtenerLocal(@PathVariable Long id) {
        log.info("--> [GET] Solicitud para obtener local con ID: {}", id);
        // Si no existe, el Service lanza ErrorNoEncontrado y el Capturador responde 404
        return ResponseEntity.ok(localService.buscarLocalPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un nuevo local", description = "Agrega un nuevo local utilizando un formato de archivo json.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Local creado de forma exitosa en el sistema"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los campos del DTO o formato JSON inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400ValidacionLocal",
                                    value = "{\"status\": 400, \"mensaje\": \"Error de validación en los campos del DTO o formato JSON inválido\", \"timestamp\": \"2026-06-17T15:40:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor de MarFuego",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500GuardarLocal",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor de MarFuego\", \"timestamp\": \"2026-06-17T15:40:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Local> guardarLocal(@Valid @RequestBody LocalDto dto) {
        log.info("--> [POST] Solicitud para guardar un nuevo local recibida.");
        // Si llegamos aquí el DTO ya está validado y no hubo errores
        Local nuevoLocal = localService.guardarLocal(dto);
        return new ResponseEntity<>(nuevoLocal, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina local por ID", description = "Elimina un local específico utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Local eliminado de forma exitosa (Sin contenido)"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID ingresado en la URL es inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400DeleteLocal",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID ingresado en la URL es inválido\", \"timestamp\": \"2026-06-17T15:45:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se puede eliminar: El local no existe en los registros",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error404DeleteLocal",
                                    value = "{\"status\": 404, \"mensaje\": \"No se puede eliminar: El local no existe en los registros\", \"timestamp\": \"2026-06-17T15:45:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500DeleteLocal",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor\", \"timestamp\": \"2026-06-17T15:45:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> eliminarLocal(@PathVariable Long id) {
        log.info("--> [DELETE] Solicitud para eliminar local con ID: {}", id);
        localService.eliminarLocal(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 (Éxito sin contenido)
    }

    // FUNCIONES PARA MESAS ---------------------------------------------

    @GetMapping("/mesas")
    @Operation(summary = "Obtener lista de mesas", description = "Busca y agrupa todas las mesas existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta de mesas exitosa"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error inesperado en el servidor al procesar la lista de mesas",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500ListarMesas",
                                    value = "{\"status\": 500, \"mensaje\": \"Error inesperado en el servidor al procesar la lista de mesas\", \"timestamp\": \"2026-06-17T15:50:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<List<Mesa>> listarMesas() {
        log.info("--> [GET] Solicitud para listar todas las mesas recibida.");
        return ResponseEntity.ok(localService.listarMesas());
    }

    // Buscar mesa por ID
    @GetMapping("/mesas/{id}")
    @Operation(summary = "Obtener mesa por ID", description = "Busca una mesa específica utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mesa encontrada con éxito"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID ingresado en la URL es inválido (debe ser un número)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400MesaIdInvalido",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID ingresado en la URL es inválido (debe ser un número)\", \"timestamp\": \"2026-06-17T15:55:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna mesa con el ID ingresado en el sistema",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error404MesaNoEncontrada",
                                    value = "{\"status\": 404, \"mensaje\": \"No se encontró ninguna mesa con el ID ingresado en el sistema\", \"timestamp\": \"2026-06-17T15:55:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500ServidorMesa",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor\", \"timestamp\": \"2026-06-17T15:55:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Mesa> obtenerMesaPorId(@PathVariable Long id) {
        log.info("--> [GET] Solicitud para obtener mesa con ID: {}", id);
        // El service lanza la excepción si no existe, el capturador hace el resto.
        return ResponseEntity.ok(localService.buscarMesaPorId(id));
    }

    @PostMapping("/mesas")
    @Operation(summary = "Agregar una nueva mesa", description = "Agrega un nueva mesa utilizando un formato de archivo json.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mesa creada de forma exitosa en el sistema"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los campos del DTO o formato JSON inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400CrearMesa",
                                    value = "{\"status\": 400, \"mensaje\": \"Error de validación en los campos del DTO o formato JSON inválido\", \"timestamp\": \"2026-06-17T16:00:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500CrearMesa",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor al intentar registrar la mesa\", \"timestamp\": \"2026-06-17T16:00:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Mesa> crearMesa(@Valid @RequestBody MesaDto dto) {
        log.info("--> [POST] Solicitud para crear una nueva mesa recibida.");
        return new ResponseEntity<>(localService.guardarMesa(dto), HttpStatus.CREATED);
    }

    //  Eliminar mesa
    @DeleteMapping("/mesas/{id}")
    @Operation(summary = "Elimina una mesa por ID", description = "Elimina una mesa específica utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mesa eliminada de forma exitosa (Sin contenido)"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID ingresado en la URL es inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400DeleteMesa",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID ingresado en la URL es inválido\", \"timestamp\": \"2026-06-17T16:05:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se puede eliminar: La mesa no existe en los registros",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error404DeleteMesa",
                                    value = "{\"status\": 404, \"mensaje\": \"No se puede eliminar: La mesa no existe en los registros\", \"timestamp\": \"2026-06-17T16:05:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500DeleteMesa",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor al intentar eliminar la mesa\", \"timestamp\": \"2026-06-17T16:05:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
        log.info("--> [DELETE] Solicitud para eliminar mesa con ID: {}", id);
        localService.eliminarMesa(id);
        return ResponseEntity.noContent().build(); // 204 No Content: Éxito sin datos que devolver
    }

    @GetMapping("/mesas/filtro/{localId}")
    @Operation(summary = "Busca y lista las mesas por su ESTADO", description = "Busca y lista las mesas específicas utilizando un estado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de mesas filtrada obtenida con éxito"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID o el valor del estado (Enum) proporcionado es inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400FiltroMesas",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID o el valor del estado (Enum) proporcionado es inválido\", \"timestamp\": \"2026-06-17T16:10:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500FiltroMesas",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor al procesar el filtrado de mesas\", \"timestamp\": \"2026-06-17T16:10:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<List<Mesa>> listarMesasDisponibles(@PathVariable Long localId, @RequestParam EstadoMesa estado) {
        log.info("--> [GET] Solicitud para filtrar mesas por local ID: {} y estado: {}", localId, estado);
        List<Mesa> mesas = localService.obtenerMesaEstadoPorLocal(localId, estado);
        return ResponseEntity.ok(mesas);
    }

    //  Cambiar solo el estado de la mesa (Actualización parcial)
    // Ejemplo: PATCH /api/locales/mesas/1/estado?nuevoEstado=OCUPADA
    @PatchMapping("/mesas/{id}/estado")
    @Operation(summary = "Cambio de Estado de mesa", description = "Busca y cambia el estado de una mesa específica utilizando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado de la mesa actualizado con éxito"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El formato del ID o el valor del nuevo estado (Enum) proporcionado es inválido",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error400PatchMesa",
                                    value = "{\"status\": 400, \"mensaje\": \"El formato del ID o el valor del nuevo estado (Enum) proporcionado es inválido\", \"timestamp\": \"2026-06-17T16:15:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró ninguna mesa con el ID ingresado para actualizar",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error404PatchMesa",
                                    value = "{\"status\": 404, \"mensaje\": \"No se encontró ninguna mesa con el ID ingresado para actualizar\", \"timestamp\": \"2026-06-17T16:15:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno inesperado en el servidor",
                    content = @Content(
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(
                                    name = "Error500PatchMesa",
                                    value = "{\"status\": 500, \"mensaje\": \"Error interno inesperado en el servidor al intentar cambiar el estado de la mesa\", \"timestamp\": \"2026-06-17T16:15:00\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Mesa> actualizarEstadoMesa(@PathVariable Long id, @RequestParam EstadoMesa nuevoEstado) {
        log.info("--> [PATCH] Solicitud para cambiar estado de mesa ID: {} a {}", id, nuevoEstado);
        Mesa mesaActualizada = localService.cambiarEstadoMesa(id, nuevoEstado);
        return ResponseEntity.ok(mesaActualizada);
    }

}
