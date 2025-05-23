package uk.co.sancode.greet.testsupport.assertions;

import org.springframework.test.web.reactive.server.FluxExchangeResult;
import uk.co.sancode.greet.model.ErrorResponse;

public class CustomAssertion {
    public static ErrorResponseAssert assertThat(FluxExchangeResult<ErrorResponse> fluxExchangeResult) {
        return new ErrorResponseAssert(fluxExchangeResult);
    }

    public static ErrorResponseAssert assertThat(ErrorResponse errorResponse) {
        return new ErrorResponseAssert(errorResponse);
    }
}
