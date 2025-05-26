package uk.co.sancode.greet.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.stereotype.Service;
import uk.co.sancode.greet.model.HelloRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.sancode.greet.config.WireMockConfiguration.GREET_BACK_PATH;
import static uk.co.sancode.greet.testsupport.RethrowAsUnchecked.uncheck;

@Service
public class WireMockService {
    private final WireMockServer wireMockServer;
    private final ObjectMapper objectMapper;

    public WireMockService(final WireMockServer wireMockServer, final ObjectMapper objectMapper) {
        this.wireMockServer = wireMockServer;
        this.objectMapper = objectMapper;
    }

    public void resetAll() {
        wireMockServer.resetAll();
    }

    public void stubHelloResponse(String name) {
        wireMockServer.stubFor(
                post(urlPathEqualTo(GREET_BACK_PATH))
                        .willReturn(ok()
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody("""
                                        {
                                            "message": "Hello %s"
                                        }
                                        """.formatted(name)
                                )
                        )
        );
    }

    public void stubHelloError(int status, String body) {
        wireMockServer.stubFor(
                post(urlPathEqualTo(GREET_BACK_PATH))
                        .willReturn(aResponse()
                                .withStatus(status)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody(body)
                        )
        );
    }

    public void verifyHelloRequest(String name) {
        var requestBody = uncheck(() -> objectMapper.writeValueAsString(new HelloRequest(name)));
        wireMockServer.verify(1,
                postRequestedFor(urlPathEqualTo(GREET_BACK_PATH))
                        .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON_VALUE))
                        .withRequestBody(equalToJson(requestBody))
        );
    }
}
