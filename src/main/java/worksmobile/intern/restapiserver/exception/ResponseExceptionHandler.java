package worksmobile.intern.restapiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import worksmobile.intern.restapiserver.error.ErrorResponse;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> ResponseErrorHandler(ResponseException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.valueOf(e.getErrorResponse().getCode()));
    }

}
