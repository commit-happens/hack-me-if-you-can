package cz.hackmeifyoucan.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERROR = "error";
    private static final String STATUS = "status";
    private static final String FIELDS = "fields";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 400);
        errorResponse.put(ERROR, "Neplatná data v požadavku");
        Map<String, String> fieldErrors = new HashMap<>();
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : errors) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        errorResponse.put(FIELDS, fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePlayerNotFound(PlayerNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 404);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleQuestionNotFound(QuestionNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 404);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PhishingCategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePhishingCategoryNotFound(PhishingCategoryNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 404);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidQuestionParameterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidQuestionParameter(InvalidQuestionParameterException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 400);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 400);
        errorResponse.put(ERROR, "Neplatná hodnota parametru: " + ex.getName());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleLogicError(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 400);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateNickname(DuplicateNicknameException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 409);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicateAnswerException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateAnswer(DuplicateAnswerException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 409);
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseConstraintError(DataIntegrityViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 409);
        errorResponse.put(ERROR, "Porušení databázového omezení");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedError(RuntimeException ex) {
        logger.error("Neočekávaná chyba serveru", ex);
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS, 500);
        errorResponse.put(ERROR, "Neočekávaná chyba serveru");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
