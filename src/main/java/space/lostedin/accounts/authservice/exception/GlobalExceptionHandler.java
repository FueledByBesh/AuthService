package space.lostedin.accounts.authservice.exception;

import space.lostedin.accounts.authservice.dto.ApiMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiMessageDTO> handleUserException(ServiceException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiMessageDTO.builder()
                        .status(ex.getStatus())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Message.class)
    public ResponseEntity<ApiMessageDTO> handleUserException(Message ex) {
        return ResponseEntity.ok(ApiMessageDTO.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessageDTO> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body(ApiMessageDTO.builder().status(500).message("An unexpected error occurred: " + ex.getMessage()).build());
    }

    @ExceptionHandler(WebClientExeption.class)
    public ResponseEntity<ApiMessageDTO> handleWebClientException(WebClientExeption ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiMessageDTO.builder().status(ex.getStatus()).message(ex.getMessage()).build());
    }

}
