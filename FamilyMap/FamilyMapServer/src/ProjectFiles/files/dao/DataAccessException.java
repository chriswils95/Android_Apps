package files.dao;

/**
 * The data access exceptions class extends the exception class and throws an error
 * if we encounter problems performning an operation in the database
 */
public class DataAccessException extends Exception {
    DataAccessException(String message) {
        super(message);
    }

    public DataAccessException() {
    }
}
