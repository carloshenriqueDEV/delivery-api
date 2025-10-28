package com.deliverytech.delivery_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex,  WebRequest request){
        
        Map<String, String> errors = new HashMap<>();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Entidade não encontrada.",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            "ENTITY_NOT_FOUND",
            errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBussinessException(BusinessException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de regra de negócio.",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            "VALIDATION_ERROR",
            errors 

        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> { 
            String fieldName = ((FieldError) error).getField(); 
            String errorMessage = error.getDefaultMessage(); 
            errors.put(fieldName, errorMessage); 
        }); 
 
        ApiErrorResponse errorResponse = new ApiErrorResponse( 
            HttpStatus.BAD_REQUEST.value(), 
            "Dados inválidos", 
            "Erro de validação nos dados enviados", 
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            "VALIDATION_ERROR",
            errors 
        ); 

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericExption(Exception ex, WebRequest request){
        Map<String, String> erros = new HashMap<>(); 
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor",
            "Ocorreu um erro inesperado",
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            "INTERNAL_ERROR",
            erros 
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

    }

    @ExceptionHandler(ConflictException.class) 
    public ResponseEntity<ApiErrorResponse> handleConflictException( 
            ConflictException ex, WebRequest request) { 
 
        Map<String, String> details = new HashMap<>(); 

        if (ex.getConflictField() != null) { 
            details.put(ex.getConflictField(), ex.getConflictValue().toString()); 
        } 
 
        ApiErrorResponse errorResponse = new ApiErrorResponse( 
            HttpStatus.CONFLICT.value(), 
            "Conflito de dados", 
            "Erro de conflito de dados",
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            "CONFLICT_ERROR",
            details 
        ); 
       
 
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    
}
