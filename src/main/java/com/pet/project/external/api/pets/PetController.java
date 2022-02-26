package com.pet.project.external.api.pets;

import static java.util.stream.Collectors.toMap;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Slf4j
class PetController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidations(
      MethodArgumentNotValidException exception) {
    var errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(it -> Map.entry(it.getField(), StringUtils.defaultString(it.getDefaultMessage())))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleEnumParsing(HttpMessageNotReadableException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @PutMapping("pet/{id}")
  @ResponseStatus(HttpStatus.OK)
  void create(@PathVariable String id, @Valid @RequestBody Pet pet) {
    log.info("Creating pet: {} with id {}", pet, id);
  }
}
