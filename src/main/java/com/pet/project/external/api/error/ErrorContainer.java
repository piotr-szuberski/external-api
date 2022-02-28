package com.pet.project.external.api.error;

import java.util.List;

public record ErrorContainer(Throwable exception, List<Error> errors) {}
