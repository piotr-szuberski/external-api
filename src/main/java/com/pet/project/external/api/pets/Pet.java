package com.pet.project.external.api.pets;

import com.pet.project.external.api.validation.NullOrNotBlank;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

record Pet(
    @NotBlank String name,
    @NotNull Kind kind,
    @NotNull @Past LocalDateTime birthDate,
    Optional<@NullOrNotBlank String> caregiver) {}
