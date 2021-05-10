package files.request;


/**
 * The login requests sets the parse json user login info into username
 * and password variables to be used in my service classes
 */
public class LoginRequest {


    /**
     * username of the attempted login user
     */
    private String userName;
    /**
     * password of the user
     */
    private String password;

    /**
     * This function returns the username
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This function returns the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * The LoginRequest constructor sets the parse json given varaibles
     * to my username and passoword variables
     * @param userName username of a login user
     * @param password password of a login user
     */
    public LoginRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
