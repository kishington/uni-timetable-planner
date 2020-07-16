package ua.com.foxminded.university.spring.service.exception;

public class InvalidDataException extends RuntimeException {
    
    private static final long serialVersionUID = 1101722093854179386L;
    
    public InvalidDataException() {
        super();
    }
    
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidDataException(Throwable cause) {
        super(cause);
    }

}
