//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package files.dao;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import files.model.Person;

/**
 *The personDAO class will performed all necessary operations in my persons table in the database
 * It also gets a secure connection to all those operations
 */
public class PersonDAO {
    /** This field is just my database connection which will be set in my constructor
     *
     */
    private static Connection conn;
    Database db;

    /**
     * The personDAO constructor sets a secured connection to my private connection variable
     * which will be used in other database operations

     */
    public PersonDAO(Database db) {
        this.db = db;
    }

    /**
     * The insert function inserts a person into my person table in the table
     * It throws an error if the connections fails, person table wasnt created or query was
     * wrong
     * @param person given person to be inserted into the database
     * @throws DataAccessException
     */
    public void insert(Person person) throws DataAccessException {
        try {
            String query = "INSERT INTO PERSONS VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = db.conn.prepareStatement(query);
            stmt.setObject(1, person.getPersonID());
            stmt.setObject(2, person.getAssociatedUserName());
            stmt.setObject(3, person.getFirstName());
            stmt.setObject(4, person.getLastName());
            stmt.setObject(5, person.getGender());
            stmt.setObject(6, person.getFatherID());
            stmt.setObject(7, person.getMotherID());
            stmt.setObject(8, person.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException var4) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }


    /**
     * The find function finds a specific person with the corresponding unique personId, and
     * returns a person model if the user is in my person table else it returns null.
     * It throws an error if the connection was corrupted, user wasnt created or data access errors
     * @param uniqueId given person id to be compare with id's in the person table in database
     * @return person found in database
     * @throws DataAccessException
     */
    public Person find(String uniqueId) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        Person person = null;
        try{
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, uniqueId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"),
                        rs.getString("AssociatedUsername"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"));

                person.setFatherID(rs.getString("fatherID"));
                person.setMotherID(rs.getString("motherID"));
                person.setSpouseID(rs.getString("spouseID"));

            }

            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return person;
    }

    /**
     * The delete person from database, deletes a person from a database that corresponds to the
     * username. It prints out an error message if the person is not in the database, and throws an
     * error if the connections fails or data access errors
     *      *                   connections with my database
     * @param username The username will be used to see if it corresponds to a user in my table
     *      *                 and delete that user
     */
    public void deletePersonFromDatabase(String username) {
        String deleteUser = "DELETE FROM PERSONS WHERE ASSOCIATEDUSERNAME LIKE '" + username + "'";

        try {
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery(deleteUser);
            if (!rs.next()) {
                System.out.println("User is not found, please register user");
            } else {
                System.out.println("user successful deleted");
            }
        } catch (SQLException var6) {
            System.out.println(var6.getMessage());
        }

    }


    /**
     * the findNum of persins in table functions, finds the number of persons in the person table by
     * looping through person and counting them. It throws an error, if the connection fails, table
     * wasnt created or dataccess errors
     *      *                   connections with my database
     * @param size This is just the size of my person table
     * @return number of persons
     */
    public int findNumOfPersonsInTable(int size) {
        String sql = "SELECT * FROM PERSONS";

        try {
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                System.out.println("Table is empty");
            }

            while(rs.next()) {
                size++;
            }
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        }
        return size;
    }


    /**
     * The findPersons by associated username functions finds all persons in my person table
     * that corresponds to given associatedusername push them into an array of persons and returns the
     * array. It throws an error if connections fails, table wasnt created or person wasnt found
     * @param associatedUsername given username to be matched with usernames in database
     * @return array of all persons with the associated username
     * @throws DataAccessException
     * @throws SQLException
     */
    public ArrayList<Person> findPersonsByAssociatedUsername(String associatedUsername) throws DataAccessException, SQLException {
        ArrayList<Person> arrayOfPerson = new ArrayList<>();
        int index = 0;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person person = new Person(rs.getString("PersonID"),
                        rs.getString("AssociatedUsername"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"));

                person.setFatherID(rs.getString("fatherID"));
                person.setMotherID(rs.getString("motherID"));
                person.setSpouseID(rs.getString("spouseID"));
                arrayOfPerson.add(person);

            }

            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return arrayOfPerson;
    }

    /**
     * This function find the spouses by looking at the spouse id
     * @param spouseID
     * @return the person whose spouse id was provided
     * @throws DataAccessException
     * @throws SQLException
     */
    public  Person findPersonsBySpouseID(String spouseID) throws DataAccessException, SQLException {
        ResultSet rs = null;
        Person person = null;
        try{
            String sql = "SELECT * FROM Persons WHERE SpouseID = ?;";

            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, spouseID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"),
                        rs.getString("AssociatedUsername"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"));

                person.setFatherID(rs.getString("fatherID"));
                person.setMotherID(rs.getString("motherID"));
                person.setSpouseID(rs.getString("spouseID"));

            }

            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return person;
    }


}
