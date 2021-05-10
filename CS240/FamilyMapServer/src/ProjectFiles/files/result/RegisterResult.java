package files.result;

import files.success.Success;

import java.io.Reader;


/**
 * The Register result class sets the given login information from the
 * register services and set them to the required register results variables
 * It also extends the success super class to include the success message
 */
public class RegisterResult extends Success  {
    /**
     * token of the user
     */
    private String authToken;
    /**
     * username of the user
     */
    private String userName;
    /**
     * id of the user
     */
    private String personID;

    /**
     * This function returns the auth token
     * @return auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * This function sets the auth token with the given auth token
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * This function returns the username
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This function sets the username
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This function returns the person id
     * @return person id
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * This function sets the person id
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }


    /**
     * The register result constructor sets the given variables to the
     * message and success variables
     * @param authToken tokens of current user
     * @param userName username of current user
     * @param personID id of current user
     * @param success success message if a user is registered
     */
    public RegisterResult(String authToken, String userName, String personID, boolean success){
        this.authToken = authToken;
        this.personID = personID;
        this.userName = userName;
        super.setSuccess(success);
    }

    /**
     * This constructor it used to set the success and register messages
     * @param message
     * @param success
     */
    public RegisterResult(String message, boolean success){

        super.setSuccess(success);
        super.setMessage(message);
    }

}
