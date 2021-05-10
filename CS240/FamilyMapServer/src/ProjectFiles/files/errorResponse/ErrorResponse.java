package files.errorResponse;


import files.success.Success;

/**
 * The error message class will be used for creating an error message and success failure message
 *
 */
public class ErrorResponse extends Success{

    /**
     * This just keeps track of my error messages
     */
    String message;

    /**
     * This constructor sets my message and success to the given message and success
     * @param message
     * @param success
     */
    public ErrorResponse(String message, boolean success){
        this. message = message;
        super.setSuccess(success);
    }
}
