package files.service;

import files.dao.*;
import files.model.Person;
import files.model.User;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


/**
 * The fill service class performs the necessary operations of the received information from my
 * request class and do some operations with my database
 */
public class FillService {

    /**
     * A registered service object that will be used to access my filled generations functions
     */
    private RegisterService registerService;
    private Database db;


    public FillService(){
        this.db = new Database();
    }

    /**
     * Th e performedFilledService first checks to see if the given username corresponds to a
     * person in the database , if found, it generates persons generations and events with the given
     * generation number
     * @param username username of the current user that needs information to be filled
     * @param generations number of generations to be generated for a user
     * @return
     * @throws DataAccessException
     */
    public String performedFilledService(String username, int generations) throws DataAccessException, SQLException {
          String result = null;
        try {
            // Open database connection
            db.startTransaction();
            registerService = new RegisterService(db);
            UserDAO userDAO = new UserDAO(db);
            PersonDAO deletePersonDAO = new PersonDAO(db);
            EventDAO deleteEventDAO = new EventDAO(db);
            User user = userDAO.find(username);
            Person person = null;
            if(user != null){
                deletePersonDAO.deletePersonFromDatabase(username);
                deleteEventDAO.deleteUserEventsFromDatabase(username);
                person = new Person(user.getPersonID(), user.getUsername(),
                        user.getFirstName(), user.getLastName(), user.getGender()
                );
                    registerService.fillPersonGenerations(person, generations, deletePersonDAO,
                            deleteEventDAO);
                            //1997, arr);
                    int numOfPersons = 1, numOfEvents = 1;
                    numOfPersons = deletePersonDAO.findNumOfPersonsInTable(numOfPersons);
                    numOfEvents = deleteEventDAO.findNumOfEventsInEventTable(numOfEvents);
                    db.closeConnection(true);
                    result = generateOutputMessage(numOfPersons, numOfEvents);
                    return result;
            }
            else {
                db.closeConnection(true);
            }

        }
        catch (DataAccessException ex) {
            db.closeConnection(false);
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * This functions returns the necessary informations needed by the fill results
     * @param numOfPersons size of the persons table
     * @param numOfEvents size of the events table
     * @return a string indicating number of persons and events added
     */
    public String generateOutputMessage(int numOfPersons, int numOfEvents) {
        String str = "Successfully added " +  numOfPersons + " persons and " + numOfEvents
        + " events to the database.";
       return str;
    }
}
