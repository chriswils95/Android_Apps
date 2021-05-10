//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


package files.model;
/**
 * The Person model class specifies the functionality that makes a user
 * like all necessary information needed for a user to be qualified as a person
 * It uses getters and setters and even parameterized constructors to set these variable
 */
public class Person {
    /**
     * These fields are just feilds that corresponds to a given user,
     * like user name, password, email, gender, and id, father id, mother id,
     * and spouse id
     */
    private String associatedUsername;
    /**
     * user id
     */
    private String personID;
    /**
     * user first name
     */
    private String firstName;
    /**
     * user last name
     */
    private String lastName;
    /**
     * user gender
     */
    private String gender;
    /**
     * user father id
     */
    private String fatherID;
    /**
     * user mother id
     */
    private String motherID;
    /**
     * user spouse id
     */
    private String spouseID;

    /**
     * Just a constructor
     */
    public Person() {
     System.out.println("person");
    }

    /**
     * This function returns the father id
     * @return father id
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * This function  sets the father id
     * @param fatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * This function gets the mother id
     * @return mother id
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * This function sets the mother id
     * @param motherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * This function gets the spouse id
     * @return spouse id
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * This function returns the associated username
     * @return associated username
     */
    public String getAssociatedUserName() {
        return associatedUsername;
    }


    /**
     * This function sets the associated username with the given username
     * @param associatedUserName
     */
    public void setAssociatedUserName(String associatedUserName) {
        this.associatedUsername = associatedUserName;
    }


    /**
     * This function returns the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This function sets the first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This function returns the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This function sets the last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    /**
     * This function sets the spouse id
     * @param spouseID
     */

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * This function returns the gender
     * @return gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * This function sets the gender
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This function returns the person id
     * @return person id
     */
    public String getPersonID() {
        return this.personID;
    }

    /**
     * This function sets the person id
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     *
     *  The person constructors sets a user with its given information. It also sets the success message by
     *   extending the success super class
     * @param personID personId that is unique to the person
     * @param associatedUserName username of the person
     * @param firstName first name of the given user
     * @param lastName  last name of the given user
     * @param gender gender of the user
     */
    public Person(String personID, String associatedUserName, String firstName, String lastName,
                  String gender) {
        this.personID = personID;
        this.associatedUsername = associatedUserName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * The personData sub class are classes that will help me in parsing
     * the json file into an array of persons
     */
    public class PersonData{
        Person[] persons;
        public int personsSize(){
            return persons.length;
        }


        /**
         * The personAtIndex function returns the person
         * object at a given index
         * @param index index of a person object in my array of persons
         * @return
         */
        public Person personAtIndex(int index){
            return persons[index];
        }
    }


    /**
     * This equals function just make sure the given object matches the person
     * @param o
     * @return true if person matches or else false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Person)) {
            return false;
        } else {
            Person oPerson = (Person)o;
            return oPerson.getPersonID().equals(this.getPersonID()) && oPerson.getSpouseID().equals(this.getSpouseID())
                    && oPerson.getAssociatedUserName().equals(this.getAssociatedUserName())
                     && oPerson.getFirstName().equals(
                            this.getFirstName()) && oPerson.getLastName().equals(this.getLastName())
                     &&
                    oPerson.getGender().equals(this.getGender());
        }
    }

}
