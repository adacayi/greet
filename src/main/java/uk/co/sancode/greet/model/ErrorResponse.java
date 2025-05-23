package uk.co.sancode.greet.model;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        List<String> validationErrors,
        String path,
        OffsetDateTime timestamp) {
}