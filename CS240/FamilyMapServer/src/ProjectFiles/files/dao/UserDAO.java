//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package files.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import files.model.Person;
import files.model.User;

/**
 * The UserDAO class performs all the necessary database access operations should will be
 * performed in my user table. It uses a secure connections to do that
 */
public class UserDAO {
    private  Connection conn;
    private Database db;

    /**
     * The UserDAO constructor sets a secured connection to my connection
     *
     */
    public UserDAO(Database db) {
        this.db = db;
    }


    /**
     * The insert function inserts a user into my user table if the table is created
     * or throws an error if it is not or the connection fails
     * @param user user to be inserted into the database
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {
        try {
            String query = "INSERT INTO Users VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = db.conn.prepareStatement(query);
            stmt.setObject(1, user.getUsername());
            stmt.setObject(2, user.getPassword());
            stmt.setObject(3, user.getFirstName());
            stmt.setObject(4, user.getLastName());
            stmt.setObject(5, user.getEmail());
            stmt.setObject(6, user.getGender());
            stmt.setObject(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException var4) {
            var4.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * the find function finds a user with corresponding username in my user table and returns
     * the User object
     * @param username given username to be matched with usernames in database
     * @return the user that matches the given username
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM USERS WHERE Username = ?;";
        User user = null;
        try{
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password")
                        ,rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getString("PersonID"));

            }

            rs.close();
            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return user;
    }
}
