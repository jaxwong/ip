package GbTheFatBoy.exception;

/**
 * Custom exception class for GbTheFatBoy application-specific errors.
 * Extends Exception to provide meaningful error messages for application failures.
 */
public class GBException extends Exception {

    /**
     * Creates a new GBException with the specified error message.
     *
     * @param message The error message describing what went wrong.
     */
    public GBException(String message) {
        super(message);
    }
}
