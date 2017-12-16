package org.apache.shiro;

import org.apache.shiro.authc.AccountException;

public class FirstLoginException extends AccountException {

    /**
     * Creates a new DisabledAccountException.
     */
    public FirstLoginException() {
        super();
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param message the reason for the exception
     */
    public FirstLoginException(String message) {
        super(message);
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public FirstLoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public FirstLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}

