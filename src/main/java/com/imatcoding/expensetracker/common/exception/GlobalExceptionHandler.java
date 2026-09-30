package com.imatcoding.expensetracker.common.exception;

import com.imatcoding.expensetracker.common.model.ApiResult;
import com.imatcoding.expensetracker.common.model.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<ResultMessage> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::mapObjectError)
                .toList();

        ApiResult<Object> apiResult = new ApiResult<>();
        apiResult.getMessages().addAll(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResult);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResult<Object>> handleAuthentication(AuthenticationException ex) {
        ApiResult<Object> apiResult = new ApiResult<>();
        apiResult.getMessages().add(new ResultMessage(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResult);
    }

    // HELPERS

    private ResultMessage mapObjectError(ObjectError error) {
        String field = (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
        return new ResultMessage(error.getDefaultMessage(), List.of(field));
    }
}
