package uk.co.sancode.greet.rest;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.co.sancode.greet.config.IntegrationTest;
import uk.co.sancode.greet.model.HelloResponse;

import static org.assertj.core.api.Assertions.assertThat;
import uk.co.sancode.greet.model.ErrorResponse;
import uk.co.sancode.greet.testsupport.assertions.CustomAssertion;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

class GreetIntegrationTest extends IntegrationTest {
    @Nested
    class Hello {
        @Test
        void shouldReturnAGreeting() {
            // Given
            var name = "John";
            var expected = new HelloResponse("Hello %s".formatted(name));

            // When
            var actual = webTestClient.get()
                    .uri("hello?name={name}", name)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .returnResult(HelloResponse.class)
                    .getResponseBody()
                    .blockFirst();

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "    "})
        void shouldResultInBadRequest_whenNameIsBlank(String name) {
            // Given
            // When
            // Then
            var result = webTestClient.get()
                    .uri("hello?name={name}", name)
                    .exchange()
                    .expectStatus()
                    .isBadRequest()
                    .returnResult(ErrorResponse.class);

            CustomAssertion.assertThat(result)
                    .hasStatus(BAD_REQUEST)
                    .hasError("Bad Request")
                    .hasMessage("Validation failed for object='helloRequest'. Error count: 1")
                    .hasValidationErrors(List.of("Error on field 'name'. Rejected value [%s]; must not be blank.".formatted(name)))
                    .hasPath("/hello")
                    .hasValidTimestamp();
        }
    }
}