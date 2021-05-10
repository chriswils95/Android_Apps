//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
/**
 * Auth token dao class implements all necessary fucntions that got to do with my auth token table
 * in my database. It implements functions like insert, find, select all etc.
 */
package files.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import files.model.AuthToken;
import files.model.Event;
/**
 * The unique tokens variables keeps track of the current user token, and the con
 * variable is just the database connectio that will be set in my constructors
 */
public class AuthTokenDAO {
    /**
     * This tokens keeps track of the user tokens
     */
    String tokens;
    /**
     * This connection is just the JDBC connections to my database that will be set in my constructor
     */
    Database db;
    /**
     * This constructor sets my connections to what ever connections is pass to it.
     */
    public AuthTokenDAO(Database db) {
        this.db = db;
    }

    /**
     * Insert functions, insert an auth token into the auth token database
     * It throws a data access exception is the table doesnt exist or the table name is mispelled
     * @param token
     * @throws DataAccessException
     */
    public void insert(AuthToken token) throws DataAccessException {
        try {
            String query = "INSERT INTO AuthTokens VALUES(?,?)";
            PreparedStatement stmt = db.conn.prepareStatement(query);
            stmt.setString(1, token.getAssociatedUsername());
            stmt.setString(2, token.getUniqueKeys());
            stmt.executeUpdate();
        } catch (SQLException var4) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }


    /**
     * The find functions finds a given username in the auth token table in my database
     * and returns the values in the table, it returns null if username is not found in the
     * database and throw any error
     * @param userName This variable will be used to see if it corresponds to usernames
     *                 in the user table
     * @return The authtoken that matches the given username
     * @throws DataAccessException
     */
    public AuthToken find(String userName) throws DataAccessException {
        ResultSet rs = null;
        AuthToken authToken = null;
        String sql = "SELECT * FROM AUTHTOKENS WHERE Username= ?;";
        try {
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                authToken = new AuthToken(rs.getString("Username"), rs.getString("UniqueKeys"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return authToken;
    }

    /**
     * The find by token function is the same as the find fucntion above but in this case
     * it looks in my authToken table for an auth token with a given tokens and result an auth token
     * if found. It throws an error if there is error with my connections, table not found,
     * or internal database erros
     * @param uniqueKeys This variable is the given tokens that wull be used to see if it matches any on my
     *                   database
     * @return
     * @throws DataAccessException
     * @throws SQLException
     */
    public AuthToken findByTokens(String uniqueKeys) throws DataAccessException, SQLException {
        AuthToken authToken = null;
        int index = 0;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM AUTHTOKENS WHERE UniqueKeys= ?;";
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, uniqueKeys);
            rs = stmt.executeQuery();
            while (rs.next()) {
                authToken = new AuthToken(rs.getString("Username"), rs.getString("UniqueKeys"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return authToken;
    }



    /**
     * The getUniqueKeys function returns the auth token keys
     * @return
     */
    public String getUniqueKeys() {
        return this.tokens;
    }
}
