package ua.com.foxminded.university.spring.dao.exception;

public class ObjectNotFoundException extends DatabaseException {
    
    private static final long serialVersionUID = -2782099598269963417L;
    
    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

}
