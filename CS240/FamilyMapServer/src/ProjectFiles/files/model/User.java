//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



package files.model;
/**
 * The User model class specifies the functionality that makes a user
 * like all necessary information needed for a user to be qualified as a user
 * It uses getters and setters and even parameterized constructors to set these variable
 */
public class User {
    /**
     * These fields are just feilds that corresponds to a given user,
     * like user name, password, email, gender, and id
     */
    private String userName;
    /**
     * user password
     */
    private String password;
    /**
     * user first name
     */
    private String firstName;
    /**
     * user last name
     */
    private String lastName;
    /**
     * user emailed
     */
    private String email;
    /**
     * user gender
     */
    private String gender;
    /**
     * user id
     */
    private String personID;

    /**
     * This function retruns the username
     * @return username
     */
    public String getUsername() {
        return this.userName;
    }

    /**
     * This function returns the first name
     * @return firstname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This function sets the given first name to first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This functions returns the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This function sets the last name of the given last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This function returns the email of the user
     * @return email
     */
    public String getEmail() {
        return email;
    }


    /**
     * This function sets the given email with the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This function returns the gender
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * This function sets the gender
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This function gets the person id
     * @return personid
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * This function sets the personId
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * this function sets the username with the given username
     * @param username
     */
    public void setUsername(String username) {
        this.userName = username;
    }

    /**
     * This function gets the password of the user
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * This function sets the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * The user constructors sets a user with its given information. It also sets the success message by
     * extending the success super class
     * @param username username of user
     * @param password password of user
     * @param first_name first name of user
     * @param last_name last name of user
     * @param email email of user
     * @param gender gender of user
     * @param personID id of user
     */
    public User(String username, String password, String first_name, String last_name, String email,
                String gender,
                String personID) {
        this.userName = username;
        this.password = password;
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * A sub class to help me parse json data into users data
     */
    public class UserData {
        /**
         * An array of users
         */
        User[] users;

        /**
         * This function returns the size of the users array
         * @return length of the users array
         */
        public int usersSize(){
            return users.length;
        }

        /**
         * This functions the value at the index of the given array
         * @param index
         * @return
         */
        public User userAtIndex(int index){
            return users[index];
        }
    }

    /**
     * The equals function just check to make sure all my user informations matches the necessary
     * user
     * @param o object of the class
     * @return true or false
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User oUser = (User)o;
            return oUser.getUsername().equals(this.getUsername()) && oUser.getPassword().equals(this.getPassword());
        }
    }
}
