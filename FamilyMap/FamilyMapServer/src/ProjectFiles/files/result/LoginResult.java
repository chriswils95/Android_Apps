package files.result;

import files.success.Success;

/**
 * The Login result class sets the given login information from the
 * loginservices and set them to the required login results variables
 * It also extends the success super class to include the success message
 */
public class LoginResult extends Success {
    /**
     * tokens of the given user
     */
    private String authToken;
    /**
     * user name of given user
     */
    private String userName;
    /**
     * id of given user
     */
    private String personID;

    /**
     * The login result constructor sets the given variables to the
     * message and success variables
     * @param authToken tokens of current user
     * @param userName username of current user
     * @param personID id of current user
     * @param success success message if user is login
     */
    public LoginResult(    boolean success,String authToken, String userName, String personID){
        super.setSuccess(success);
        this.authToken = authToken;
        this.personID = personID;
        this.userName = userName;

    }

    /**
     * This function sets the constructor for success and error messages
     * @param message
     * @param success
     */
    public LoginResult(String message, boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }
}
