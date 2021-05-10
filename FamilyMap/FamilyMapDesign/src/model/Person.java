//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package model;

public class Person {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String personID;

    public Person(String username, String password, String first_name, String last_name, String email, String gender, String personID) {
        this.userName = username;
        this.password = password;
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return this.userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return this.firstName;
    }

    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    public String getLast_name() {
        return this.lastName;
    }

    public void setLast_name(String last_name) {
        this.lastName = last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return this.personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Person)) {
            return false;
        } else {
            Person oPerson = (Person)o;
            return oPerson.getUsername().equals(this.getUsername()) && oPerson.getPassword().equals(this.getPassword())
                    && oPerson.getFirst_name().equals(this.getFirst_name()) && oPerson.getLast_name().equals(this.getLast_name())
                    && oPerson.getEmail().equals(this.getEmail()) && oPerson.getGender().equals(this.getGender())
                    && oPerson.getPersonID().equals(this.getPersonID());
        }
    }
}
