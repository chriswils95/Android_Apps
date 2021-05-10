package files.service;

import files.dao.AuthTokenDAO;
import files.dao.DataAccessException;
import files.dao.Database;
import files.dao.PersonDAO;
import files.model.AuthToken;
import files.model.Person;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The persons service class performs the necessary operations of the received information from my
 * request class and clear all data in my database
 */
public class PersonService {

    Database db;

    public PersonService(){
        db = new Database();
    }

    /**
     * The performPersonService persons necessary operations for a single person
     * It checks to make sure the person is in the database and returns the necessary persons
     * information in the person table
     * @param personID id of a user that will be matched with id's in the database
     * @return
     * @throws DataAccessException
     */
    public Person performPersonService(String personID, String authToken) throws DataAccessException, SQLException {
        Person person = null;
        try{
            db.startTransaction();
            PersonDAO personDAO = new PersonDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            AuthToken authToken1 = authTokenDAO.findByTokens(authToken);
            person = personDAO.find(personID);
            if(authToken1 == null){
                db.closeConnection(false);
                return null;
            }
            else if(person != null && (authToken1.getAssociatedUsername().equals(
                    person.getAssociatedUserName()
            ))){
                db.closeConnection(true);
                return person;
            }
            person = null;
            db.closeConnection(true);
        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
        return person;
    }

    /**
     * The perform service on multiple persons checks to see if the given auth token matches a user
     * in the database and find the given username that corresponds to all persons in my person table
     * @param authToken tokens of a user that will be matched with tokens in database
     * @return
     * @throws DataAccessException
     */
    public Person [] performServiceOnMuliplePerson(String authToken) throws DataAccessException, SQLException {
        ArrayList<Person> arraysOfPerson = new ArrayList<>();
        Person [] person = null;
        try{
            db.startTransaction();
            PersonDAO personDAO = new PersonDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            AuthToken authTokenObject = authTokenDAO.findByTokens(authToken);
            if(authTokenObject != null){
                arraysOfPerson = personDAO.findPersonsByAssociatedUsername(authTokenObject.
                        getAssociatedUsername());
                person = new Person[arraysOfPerson.size()];

                person = arraysOfPerson.toArray(person);
            }
            db.closeConnection(true);
        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
        return person;
    }
}
