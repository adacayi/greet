package uk.co.sancode.greet.rest;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.co.sancode.greet.config.IntegrationTest;
import uk.co.sancode.greet.model.HelloResponse;

import static org.assertj.core.api.Assertions.assertThat;

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
    }
}