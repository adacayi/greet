package uk.co.sancode.greet.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import uk.co.sancode.greet.model.HelloRequest;
import uk.co.sancode.greet.model.HelloResponse;
import uk.co.sancode.greet.model.greetback.GreetBackRequest;
import uk.co.sancode.greet.model.greetback.GreetBackResponse;

@RestController
public class HelloController {
    private final WebClient webClient;
    private final String greetBackUrl;

    public HelloController(WebClient webClient,
                           @Value("${greet-back-url}") String greetBackUrl) {
        this.webClient = webClient;
        this.greetBackUrl = greetBackUrl;
    }

    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello(@Valid HelloRequest request) {
        var greetBackRequest = new GreetBackRequest(request.name());

        var response = webClient
                .post()
                .uri(greetBackUrl)
                .bodyValue(greetBackRequest)
                .retrieve()
                .bodyToMono(GreetBackResponse.class)
                .block();

        return ResponseEntity.ok(new HelloResponse("Response from greet-back: " + (response == null ?  "": response.message())));
    }
}
