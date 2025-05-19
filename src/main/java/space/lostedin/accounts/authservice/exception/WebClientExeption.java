package space.lostedin.accounts.authservice.exception;

import lombok.Getter;

@Getter
public class WebClientExeption extends RuntimeException{

    private final int status;
    private final String message;

    public WebClientExeption(int status, String message) {
        super("HTTP " + status + ": " + message);
        this.status = status;
        this.message = message;
    }

}
