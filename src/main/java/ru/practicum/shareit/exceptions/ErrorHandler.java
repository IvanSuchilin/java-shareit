package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exceptions.BookingExceptions.InvalidBookingDtoException;
import ru.practicum.shareit.exceptions.BookingExceptions.ValidationFailedException;
import ru.practicum.shareit.exceptions.itemExceptions.InvalidItemDtoException;
import ru.practicum.shareit.exceptions.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.userExceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.response.ErrorResponse;

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

    @ExceptionHandler({InvalidEmailException.class, InvalidItemDtoException.class, InvalidBookingDtoException.class,
            ValidationFailedException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateEmailException(final RuntimeException e) {
        log.debug("Возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EmailAlreadyExistException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleValidateEmailExistException(final RuntimeException e) {
        log.debug("Возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, ResponseStatusException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final RuntimeException e) {
        log.debug("Возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
