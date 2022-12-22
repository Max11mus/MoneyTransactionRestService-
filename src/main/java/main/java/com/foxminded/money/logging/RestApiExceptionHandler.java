package main.java.com.foxminded.money.logging;

import main.java.com.foxminded.money.exeptions.ErrorResponce;
import main.java.com.foxminded.money.exeptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(RestApiExceptionHandler.class);

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponce errorResponce = getErrorResponceForBadRequest(ex);
        LOGGER.error(ex.getMessage(), ex.getCause());

        return new ResponseEntity<>(errorResponce, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponce errorResponce = getErrorResponceForBadRequest(ex);
        LOGGER.error(ex.getMessage(), ex.getCause());

        return new ResponseEntity<>(errorResponce, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ServiceUnavailableException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponce> handleServerError(RuntimeException e) {
        ErrorResponce errorResponce = new ErrorResponce(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                "Server error response code indicates that the server is not ready to handle the request.");

        LOGGER.error(e.getMessage(), e.getCause());

        return new ResponseEntity<ErrorResponce>(errorResponce, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler({ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponce> handleBadRequestError(RuntimeException e) {
        ErrorResponce errorResponce = getErrorResponceForBadRequest(e);

        LOGGER.error(e.getMessage(), e.getCause());

        return new ResponseEntity<ErrorResponce>(errorResponce, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponce getErrorResponceForBadRequest(Throwable ex) {
        ErrorResponce errorResponce = new ErrorResponce(HttpStatus.BAD_REQUEST, ex.getMessage(),
                "The server cannot or will not process the request due to something "
                        + "that is perceived to be a client error (e.g., malformed request syntax, "
                        + "invalid request message framing, or deceptive request routing).");
        return errorResponce;
    }
}
