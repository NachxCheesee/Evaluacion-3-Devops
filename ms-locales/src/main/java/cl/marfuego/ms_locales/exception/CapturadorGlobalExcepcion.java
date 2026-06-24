package cl.marfuego.ms_locales.exception;

import cl.marfuego.ms_locales.dto.ErrorDto;
import cl.marfuego.ms_locales.exception.custom.ErrorNoEncontrado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CapturadorGlobalExcepcion {

    private static final Logger log = LoggerFactory.getLogger(CapturadorGlobalExcepcion.class);

    // Atrapamos errores de "No encontrado" (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> manejarErrorLogica(RuntimeException ex) {
        log.warn("⚠️ [400 BAD REQUEST] RuntimeException capturada en Handler: {}", ex.getMessage());
        // Creamos el objeto usando tu constructor manual
        ErrorDto error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorNoEncontrado.class)
    public ResponseEntity<ErrorDto> manejarRecursoNoEncontrado(ErrorNoEncontrado ex) {
        log.warn("⚠️ [404 NOT FOUND] Recurso no encontrado: {}", ex.getMessage());
        ErrorDto error = new ErrorDto(
                HttpStatus.NOT_FOUND.value(), // 404
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

     // Errores de campos no validos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> manejarValidaciones(MethodArgumentNotValidException ex) {
        // Buscamos el error, si no hay, ponemos un mensaje genérico
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Dato inválido");

        log.warn("⚠️ [400 BAD REQUEST] Error de validación DTO: {}", mensaje);

        ErrorDto error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(), // 400
                mensaje,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Atrapamos cualquier otro error inesperado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> manejarErrorGeneral(Exception ex) {
        log.error("💥 [500 INTERNAL SERVER ERROR] Error crítico inesperado en MarFuego: ", ex);
        ErrorDto error = new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado en el sistema MarFuego",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> manejarErrorLectura(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.warn("⚠️ [400 BAD REQUEST] Error de lectura HTTP (JSON inválido o Enum incorrecto): {}", ex.getMessage());        ErrorDto error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                "Error en el formato del JSON o valor de Enum inválido",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> manejarErrorTipoDato(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        // Creamos un mensaje amigable
        String nombreParametro = ex.getName();
        String valorEnviado = ex.getValue() != null ? ex.getValue().toString() : "nulo";
        String mensaje = String.format("El parámetro '%s' tiene un valor inválido: '%s'",
                nombreParametro, valorEnviado);
        log.warn("⚠️ [400 BAD REQUEST] Tipo de dato incorrecto en parámetro URL: {}", mensaje);
        ErrorDto error = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                mensaje,
                java.time.LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
