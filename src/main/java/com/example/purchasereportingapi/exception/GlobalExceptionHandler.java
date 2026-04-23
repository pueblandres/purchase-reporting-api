package com.example.purchasereportingapi.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                            HttpServletRequest request) {
        List<ValidationErrorResponse> validations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "Hay errores de validación en la solicitud",
                request.getRequestURI(),
                validations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                     HttpServletRequest request) {
        List<ValidationErrorResponse> validations = ex.getConstraintViolations()
                .stream()
                .map(this::toValidationError)
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "Hay errores de validación en la solicitud",
                request.getRequestURI(),
                validations);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex,
                                                                  HttpServletRequest request) {
        List<ValidationErrorResponse> validations = List.of(new ValidationErrorResponse(
                ex.getParameterName(),
                "El parámetro " + ex.getParameterName() + " es obligatorio",
                null));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Faltan parámetros obligatorios en la solicitud",
                request.getRequestURI(),
                validations);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                              HttpServletRequest request) {
        List<ValidationErrorResponse> validations = List.of(new ValidationErrorResponse(
                ex.getName(),
                "El parámetro " + ex.getName() + " tiene un formato inválido",
                ex.getValue()));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Hay parámetros con formato inválido",
                request.getRequestURI(),
                validations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex,
                                                                   HttpServletRequest request) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "El cuerpo de la solicitud no es válido o tiene un formato JSON incorrecto",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                               HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Conflict",
                "La solicitud genera un conflicto con datos existentes",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(ReportGenerationException.class)
    public ResponseEntity<ApiErrorResponse> handleReportGeneration(ReportGenerationException ex,
                                                                  HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Error inesperado",
                request.getRequestURI(),
                null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error, String message,
                                                          String path,
                                                          List<ValidationErrorResponse> validations) {
        return ResponseEntity.status(status)
                .body(ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .path(path)
                        .validations(validations)
                        .build());
    }

    private ValidationErrorResponse toValidationError(FieldError fieldError) {
        return new ValidationErrorResponse(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue());
    }

    private ValidationErrorResponse toValidationError(ConstraintViolation<?> violation) {
        return new ValidationErrorResponse(
                getLastPathSegment(violation.getPropertyPath().toString()),
                violation.getMessage(),
                violation.getInvalidValue());
    }

    private String getLastPathSegment(String propertyPath) {
        int lastDotIndex = propertyPath.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == propertyPath.length() - 1) {
            return propertyPath;
        }
        return propertyPath.substring(lastDotIndex + 1);
    }
}
