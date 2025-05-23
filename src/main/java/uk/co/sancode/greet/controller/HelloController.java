package uk.co.sancode.greet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.sancode.greet.model.HelloRequest;
import uk.co.sancode.greet.model.HelloResponse;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello(@Valid HelloRequest request) {
        return ResponseEntity.ok(new HelloResponse("Hello %s".formatted(request.name())));
    }
}
