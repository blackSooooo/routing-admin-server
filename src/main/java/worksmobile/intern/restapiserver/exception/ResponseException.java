package worksmobile.intern.restapiserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import worksmobile.intern.restapiserver.error.ErrorResponse;

@Getter
@AllArgsConstructor
public class ResponseException extends RuntimeException{
    private ErrorResponse errorResponse;
}
