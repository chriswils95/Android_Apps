package files.result;

import files.model.Person;
import files.success.Success;

/**
 * The persons result class sets the given person information from the
 * persons services and set them to the required person results variables
 * It also extends the success super class to include the success message
 */

public class PersonsResult extends Success {

    /**
     * array of all family members of a user
     */
    Person [] data;

    /**
     * Object of a single user
     */
    Person person;
    /**
     * username of a user
     */
    private String associatedUsername;
    /**
     * id of a user
     */
    private String personID;
    /**
     * first name of user
     */
    private String firstName;
    /**
     * last name of user
     */
    private String lastName;
    /**
     * gender of user
     */
    private String gender;
    /**
     * father id of user
     */
    private String fatherID;
    /**
     * mother id of user
     */
    private String motherID;
    /**
     * spouse id of user
     */
    private String spouseID;


    /**
     * This function sets the father id
     * @param fatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * This function sets the mother id with the given mother id
     * @param motherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * This function sets the spouse id
     * @param spouseID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * This function sets the person constructor for success messages and person arrays
     * @param persons
     * @param success
     */
    public PersonsResult(Person [] persons, boolean success){
        this.data = persons;
        super.setSuccess(success);
    }

    /**
     * The person result constructor sets the given variables to the
     * message and success variables
     * @param personID id of current user
     * @param associatedUserName username of current username
     * @param firstName first name of current user
     * @param lastName last name of current
     * @param gender gender of current user
     * @param success success message if a user is find
     */
    public PersonsResult(String personID, String associatedUserName, String firstName, String lastName,
                         String gender, boolean success) {
        this.personID = personID;
        this.associatedUsername = associatedUserName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        super.setSuccess(success);
    }

    /**
     * This function sets the constructor for success and error message use when finding
     * a single person
     * @param message
     * @param success
     */
    public PersonsResult(String message, boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }

    /**
     * This function returns the persons array
     * @return array of persons
     */
    public Person[] getData() {
        return data;
    }
}
