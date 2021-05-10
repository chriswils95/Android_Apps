package files.service;



import files.dao.*;
import files.model.AuthToken;
import files.model.Event;
import files.model.Person;
import files.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
/**
 * The clear service class performs the necessary operations of the received information from my
 * request class and clear all data in my database
 */
public class ClearService {
    /**
     * These fields keep track of the number of users, persons, and
     * events in my tables
     */
    private int numOfUsers, numOfPersons, numOfEvents;
    Database db;
    static final String EVENT_CLEAR_TYPE = "Events";
    static final String PERSON_CLEAR_TYPE = "Persons";
    static final String USER_CLEAR_TYPE = "Users";
    static final String AUTHTOKEN_CLEAR_TYPE = "AuthTokens";

    /**
     * This constructor just initialized my variables
     */
    public ClearService(){
        numOfEvents = 0;
        numOfPersons = 0;
        numOfUsers = 0;
        db = new Database();
    }

    /**
     * This function clears all data in my table,
     * and throws an error is connection fails or no table is created
     * @throws DataAccessException
     */
    public void performClearService() throws DataAccessException, SQLException {
        try{
            db.startTransaction();
            db.clearTables(EVENT_CLEAR_TYPE);
            db.clearTables(PERSON_CLEAR_TYPE);
            db.clearTables(USER_CLEAR_TYPE);
            db.clearTables(AUTHTOKEN_CLEAR_TYPE);
            db.closeConnection(true);

        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
    }



}
