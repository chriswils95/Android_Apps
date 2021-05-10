package files.service;

import files.model.AuthToken;
import files.model.Event;
import files.model.Person;
import files.model.User;
import files.dao.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The load service performs the necessary operations of the received information from my
 * request class and do some operations with my database
 */
public class LoadService {

    /**
     * These fields keep track of the number of users, persons, and
     * event
     * */
    private int numOfUsers, numOfPersons, numOfEvents;
    Database db;
    /**
     * This constructor initializes my private variables
     */
    public LoadService(){
        numOfEvents = 0;
        numOfPersons = 0;
        numOfUsers = 0;
        db = new Database();
    }

    /**
     * The LoadDataIntoDatabase loads the given data in the corresponding tables
     * It loops through each arrays and insert them directly into the database
     * @param events arrays of events that will be loaded into the databse
     * @param person arrays of person that will be loaded into the database
     * @param user arrays of users that will be loaded into the databse
     * @throws DataAccessException
     */
    public void loadDataIntoDatabase(Event[] events, Person [] person,
                                     User[] user) throws DataAccessException, SQLException {
        try{
            db.startTransaction();
            UserDAO userDAO = new UserDAO(db);
            PersonDAO personDAO = new PersonDAO(db);
            EventDAO eventDAO = new EventDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            AuthToken authToken = null;
            numOfPersons = person.length;
            numOfUsers = user.length;
            numOfEvents = events.length;
            for(int i = 0; i < user.length;i++){
                userDAO.insert(user[i]);
                String token = UUID.randomUUID().toString();
                authToken = new AuthToken(user[i].getUsername(), token);
                authTokenDAO.insert(authToken);
            }

            for(int i = 0; i < events.length; i++){
                eventDAO.insert(events[i]);
            }

            for(int i = 0; i < person.length; i++){
                personDAO.insert(person[i]);
            }
            db.closeConnection(true);

        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }

    }


    /**
     * This to string function returns the necessary information in a string like
     * num of users, events etc.
     */
    @Override
    public String toString() {
        String str =  "Successfully added " + numOfUsers  +
                " users, " + numOfPersons + " persons, and " + numOfEvents + " events to the database.";
        return str;
    }

}
