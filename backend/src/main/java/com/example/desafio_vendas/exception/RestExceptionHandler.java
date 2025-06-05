package com.example.desafio_vendas.exception;

import com.example.desafio_vendas.dto.DetalheErro; 
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DetalheErro> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        DetalheErro erro = new DetalheErro(
                "Recurso Não Encontrado",                     
                HttpStatus.NOT_FOUND.value(),                
                ex.getMessage(),                             
                LocalDateTime.now(),                         
                "Recurso não pôde ser localizado no caminho: " + request.getRequestURI() // mensagemDesenvolvedor
        );
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DetalheErro> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String detalhesDosCamposComErro = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "'" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        DetalheErro erro = new DetalheErro(
                "Erro de Validação na Requisição",            
                HttpStatus.BAD_REQUEST.value(),              
                "Um ou mais campos enviados são inválidos.", 
                LocalDateTime.now(),                        
                "Detalhes dos campos: " + detalhesDosCamposComErro + ". Caminho: " + request.getRequestURI() // mensagemDesenvolvedor
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DetalheErro> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        System.err.println("LOG: Erro inesperado capturado pelo Handler Genérico: " + ex.getClass().getName() + " - " + ex.getMessage());

        DetalheErro erro = new DetalheErro(
                "Erro Interno do Servidor",                  
                HttpStatus.INTERNAL_SERVER_ERROR.value(),    
                "Ocorreu um erro inesperado ao processar sua solicitação. Por favor, tente novamente mais tarde.",
                LocalDateTime.now(),                         
                "Exceção: " + ex.getClass().getSimpleName() + " no caminho: " + request.getRequestURI() // mensagemDesenvolvedor
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
