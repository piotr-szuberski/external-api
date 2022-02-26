package com.pet.project.external.api.pets;

import com.pet.project.external.api.validation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Builder;

record Pet(
    @NotBlank String name,
    @NotNull Kind kind,
    @NotNull @Past LocalDateTime birthDate,
    Optional<@NullOrNotBlank String> caregiver
) {

  @Builder
  public Pet {
  }
}
