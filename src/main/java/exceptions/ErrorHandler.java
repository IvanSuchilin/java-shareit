package exceptions;

import exceptions.userExceptions.EmailAlreadyExistException;
import exceptions.userExceptions.InvalidEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final IllegalArgumentException e) {
        log.debug("Возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final NullPointerException e) {
        log.debug("Возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({InvalidEmailException.class, EmailAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final RuntimeException e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
