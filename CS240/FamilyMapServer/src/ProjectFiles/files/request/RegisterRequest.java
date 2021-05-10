package files.request;

import java.io.*;


/**
 *
 * The register requests sets the parse json user user info into
 * the user model format to be used to be used in my services class to
 * do some implementations
 *
 */
public class RegisterRequest {

    /**
     * username of the register user
     */
    private String userName;
    /**
     * password of the register user
     */
    private String password;
    /**
     * first name of the registered user
     */
    private String firstName;
    /**
     * last name of the registered user
     */
    private String lastName;
    /**
     * email of the registered user
     */
    private String email;
    /**
     * gender of the registered user
     */
    private String gender;
    /**
     * id of the registered user
     */
    private String personID;

    /**
     * The Register request constructor sets the given user info to my correspinding
     * variables
     * @param userName username of the current user to be register
     * @param password password of the current user to be register
     * @param firstName first name of the current user to be register
     * @param lastName last name of the current user to be register
     * @param email email of the current user to be register
     * @param gender gender of the current user to be register
     * @param personID id of the current user to be register
     */
    public RegisterRequest(String userName, String password, String firstName, String lastName, String email,
                           String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.personID = personID;
    }


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
     * This function returns the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This function returns the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This function returns the email
     * @return email
     */
    public String getEmail() {
        return email;
    }


    /**
     * This function returns
     * @return
     */
    public String getGender() {
        return gender;
    }

}
