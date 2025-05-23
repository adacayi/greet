package uk.co.sancode.greet.testsupport.assertions;

import org.assertj.core.api.AbstractAssert;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import uk.co.sancode.greet.model.ErrorResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseAssert extends AbstractAssert<ErrorResponseAssert, ErrorResponse> {
    public ErrorResponseAssert(ErrorResponse errorResponse) {
        super(errorResponse, ErrorResponseAssert.class);
    }

    public ErrorResponseAssert(FluxExchangeResult<ErrorResponse> fluxResult) {
        this(fluxResult.getResponseBody().blockFirst());
    }

    public ErrorResponseAssert hasStatus(int status) {
        isNotNull();

        var actualStatus = actual.status();

        assertThat(actualStatus)
                .as("Expected status to be %d but was %d", status, actualStatus)
                .isEqualTo(status);

        return this;
    }

    public ErrorResponseAssert hasStatus(HttpStatus status) {
        isNotNull();
        assertThat(status).as("Expected status cannot be null").isNotNull();

        return hasStatus(status.value());
    }

    public ErrorResponseAssert hasError(String error) {
        isNotNull();
        var actualError = actual.error();

        assertThat(actualError).as("Expected error to be \"%s\", but was \"%s\"", error, actualError).isEqualTo(error);

        return this;
    }

    public ErrorResponseAssert hasMessage(String message) {
        isNotNull();
        var actualMessage = actual.message();

        assertThat(actualMessage).as("Expected message to be \"%s\", but was \"%s\"", message, actualMessage).isEqualTo(message);

        return this;
    }

    public ErrorResponseAssert hasValidationErrors(List<String> errors) {
        isNotNull();
        var actualErrors = actual.validationErrors();

        assertThat(actualErrors).as("Expected errors to be %s, but was %s", getStringList(errors), getStringList(actualErrors)).containsExactlyInAnyOrderElementsOf(errors);

        return this;
    }

    public ErrorResponseAssert hasPath(String path) {
        isNotNull();
        var actualPath = actual.path();

        assertThat(actualPath).as("Expected path to be \"%s\", but was \"%s\"", path, actualPath).isEqualTo(path);

        return this;
    }

    public ErrorResponseAssert hasValidTimestamp() {
        isNotNull();
        var now = OffsetDateTime.now();
        return hasTimestampNotBefore(now.minusMinutes(1))
                .hasTimestampNotAfter(now);
    }

    public ErrorResponseAssert hasTimestampNotBefore(OffsetDateTime timestamp) {
        isNotNull();
        var actualTimestamp = actual.timestamp();

        assertThat(actualTimestamp).as("Expected timestamp to be not before \"%s\", but was \"%s\"", timestamp, actualTimestamp).isAfterOrEqualTo(timestamp);

        return this;
    }

    public ErrorResponseAssert hasTimestampNotAfter(OffsetDateTime timestamp) {
        isNotNull();
        var actualTimestamp = actual.timestamp();

        assertThat(actualTimestamp).as("Expected timestamp to be not after \"%s\", but was \"%s\"", timestamp, actualTimestamp).isBeforeOrEqualTo(timestamp);

        return this;
    }

    private String getStringList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "[]";
        }

        return values.stream().collect(Collectors.joining("\", \"", "[\"", "\"]"));
    }
}
