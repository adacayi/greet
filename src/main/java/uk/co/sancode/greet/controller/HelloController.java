package uk.co.sancode.greet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.sancode.greet.model.HelloResponse;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello(@RequestParam String name) {
        return ResponseEntity.ok(new HelloResponse("Hello %s".formatted(name)));
    }
}
