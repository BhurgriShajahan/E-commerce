package com.auth.exception;
import com.customutility.model.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJwtException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CustomResponseEntity<Object> handleJwtException(CustomJwtException ex) {
        // Return the exception message with an appropriate HTTP status
        return new CustomResponseEntity<>(ex.getMessage());
    }
    // You can add more exception handlers here for other exceptions


    public CustomResponseEntity<Object> handleNotFoundException(CustomJwtException ex) {
        return new CustomResponseEntity<>(1000, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomResponseEntity<Object> handleGenericException(Exception ex) {
        return CustomResponseEntity.error(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomResponseEntity handleValidationErrors(MethodArgumentNotValidException ex)
    {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        return new CustomResponseEntity<>(404, "Field Error", getErrorsMap(errors));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}

