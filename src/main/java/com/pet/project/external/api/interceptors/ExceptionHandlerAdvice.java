package com.pet.project.external.api.interceptors;

import com.pet.project.external.api.error.Error;
import com.pet.project.external.api.error.ErrorCode;
import com.pet.project.external.api.error.ErrorDto;
import com.pet.project.external.api.error.ErrorMapper;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException exception) {
    var errorContainer = ErrorMapper.errorContainerFromValidations(exception);
    return ResponseEntity.badRequest().body(ErrorMapper.errorDto(errorContainer));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleEnumParsing(HttpMessageNotReadableException exception) {
    var error = new Error("", exception.getLocalizedMessage());
    var errorDto = new ErrorDto(ErrorCode.INPUT_VALIDATION_ERROR, List.of(error));
    return ResponseEntity.badRequest().body(errorDto);
  }
}
