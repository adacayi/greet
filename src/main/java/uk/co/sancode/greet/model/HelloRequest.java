package uk.co.sancode.greet.model;

import jakarta.validation.constraints.NotBlank;

public record HelloRequest(@NotBlank String name) {
}
