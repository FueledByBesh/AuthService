package space.lostedin.accounts.authservice.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final int status;

    public ServiceException(int status, String message) {
        super(message);
        this.status = status;
    }

}
