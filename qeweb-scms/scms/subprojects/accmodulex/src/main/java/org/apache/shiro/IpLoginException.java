package org.apache.shiro;

import org.apache.shiro.authc.AccountException;

public class IpLoginException extends AccountException {

    /**
     * Creates a new DisabledAccountException.
     */
    public IpLoginException() {
        super();
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param message the reason for the exception
     */
    public IpLoginException(String message) {
        super(message);
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public IpLoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DisabledAccountException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public IpLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}

