package ua.com.foxminded.university.spring.dao.exception;

public class DatabaseException extends Exception {
    
    private static final long serialVersionUID = -4041899048452167994L;

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

}
