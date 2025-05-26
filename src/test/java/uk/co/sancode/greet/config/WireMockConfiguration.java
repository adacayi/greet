package uk.co.sancode.greet.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Configuration
public class WireMockConfiguration {
    private WireMockServer wireMockServer;
    public static final String GREET_BACK_PATH = "/greet-back";

    @Bean
    public WireMockServer wireMockServer(final ConfigurableApplicationContext applicationContext) {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        TestPropertyValues.of(
                        "greet-back-url: http://localhost:%d%s".formatted(wireMockServer.port(), GREET_BACK_PATH))
                .applyTo(applicationContext);

        return wireMockServer;
    }

    @PreDestroy
    public void preDestroy() {
        wireMockServer.shutdown();
    }
}
