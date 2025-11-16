// Jednoduchý centrální handler chyb pro celé API

package cz.hackmeifyoucan.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Klíče v JSON odpovědích (aby se neopakovaly literály)
    private static final String ERROR = "error";
    private static final String FIELDS = "fields";

    // 400 Bad Request pro validační chyby (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, "Neplatná data v požadavku");
        Map<String, String> fieldErrors = new HashMap<>();
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : errors) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        errorResponse.put(FIELDS, fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 400 Bad Request pro logické chyby (např. hráč neexistuje)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleLogicError(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(ERROR, ex.getMessage()));
    }

    // 409 Conflict pro porušení databázových omezení (např. unikátní přezdívka)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseConstraintError(
            DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(ERROR, "Porušení databázového omezení (např. unikátní přezdívka)"));
    }

    // 500 Internal Server Error pro ostatní neočekávané chyby
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(ERROR, "Neočekávaná chyba serveru"));
    }
}
