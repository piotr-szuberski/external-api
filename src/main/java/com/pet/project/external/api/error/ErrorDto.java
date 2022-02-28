package com.pet.project.external.api.error;

import java.util.List;

public record ErrorDto(ErrorCode code, List<Error> errors) {}
