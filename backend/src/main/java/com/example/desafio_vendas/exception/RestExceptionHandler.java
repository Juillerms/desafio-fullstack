package com.example.desafio_vendas.exception;

import com.example.desafio_vendas.dto.DetalheErro; // Seu DTO existente
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

    // 1. Tratamento para sua exceção customizada ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DetalheErro> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        DetalheErro erro = new DetalheErro(
                "Recurso Não Encontrado",                     // titulo
                HttpStatus.NOT_FOUND.value(),                // status
                ex.getMessage(),                             // detalhe (mensagem da sua exceção)
                LocalDateTime.now(),                         // timestamp
                "Recurso não pôde ser localizado no caminho: " + request.getRequestURI() // mensagemDesenvolvedor
        );
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    // 2. Tratamento para erros de validação (@Valid nos DTOs de entrada)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DetalheErro> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        // Coleta os erros de validação de forma simples
        String detalhesDosCamposComErro = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "'" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        DetalheErro erro = new DetalheErro(
                "Erro de Validação na Requisição",            // titulo
                HttpStatus.BAD_REQUEST.value(),              // status
                "Um ou mais campos enviados são inválidos.", // detalhe (mensagem genérica para o usuário)
                LocalDateTime.now(),                         // timestamp
                "Detalhes dos campos: " + detalhesDosCamposComErro + ". Caminho: " + request.getRequestURI() // mensagemDesenvolvedor
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // 3. Tratamento genérico para qualquer outra exceção não tratada (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DetalheErro> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        // É uma boa prática logar a exceção original no servidor para depuração
        System.err.println("LOG: Erro inesperado capturado pelo Handler Genérico: " + ex.getClass().getName() + " - " + ex.getMessage());
        // ex.printStackTrace(); // Para ver o stack trace completo no console durante o desenvolvimento

        DetalheErro erro = new DetalheErro(
                "Erro Interno do Servidor",                  // titulo
                HttpStatus.INTERNAL_SERVER_ERROR.value(),    // status
                "Ocorreu um erro inesperado ao processar sua solicitação. Por favor, tente novamente mais tarde.", // detalhe (mensagem amigável)
                LocalDateTime.now(),                         // timestamp
                "Exceção: " + ex.getClass().getSimpleName() + " no caminho: " + request.getRequestURI() // mensagemDesenvolvedor
                // Poderia adicionar ex.getMessage() aqui se for seguro e útil para o desenvolvedor.
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
