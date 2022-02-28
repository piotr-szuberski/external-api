package com.pet.project.external.api.pets;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

  @PutMapping("pets/{id}")
  @ResponseStatus(HttpStatus.OK)
  void create(@PathVariable String id, @Valid @RequestBody Pet pet) {
    log.info("Creating pet: {} with id {}", pet, id);
  }
}
