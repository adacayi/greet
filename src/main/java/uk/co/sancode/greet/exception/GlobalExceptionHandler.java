package uk.co.sancode.greet.exception;

import jakarta.servlet.RequestDispatcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.co.sancode.greet.config.ApplicationErrorAttributes;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ApplicationErrorAttributes applicationErrorAttributes;

    public GlobalExceptionHandler(ApplicationErrorAttributes applicationErrorAttributes) {
        this.applicationErrorAttributes = applicationErrorAttributes;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(ex, headers, status, request);
    }

    private ResponseEntity<Object> getResponseEntity(Exception ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value(), RequestAttributes.SCOPE_REQUEST);
        var body = applicationErrorAttributes.getErrorResponse(request);

        return handleExceptionInternal(ex, body, headers, status, request);
    }
}
