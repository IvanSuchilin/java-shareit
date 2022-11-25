package exceptions.userExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class EmailAlreadyExistException extends RuntimeException {

        public EmailAlreadyExistException(String message) {
            super(message);
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }
    }