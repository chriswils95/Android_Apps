package files.exceptions;

import javax.naming.AuthenticationException;

/**
 * This exception classes extents the authentication exceptions and will be used to throw an
 * exception whenever a user, events or tokens is not found in database
 */
public class UserAlreadyExistAuthenticationException extends AuthenticationException {

    public UserAlreadyExistAuthenticationException(final String msg) {
        super(msg);
    }

}
