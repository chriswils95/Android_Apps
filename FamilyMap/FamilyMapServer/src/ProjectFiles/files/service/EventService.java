package files.service;

import files.dao.*;
import files.model.AuthToken;
import files.model.Event;
import files.model.Person;

import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * The event service  class performs the necessary operations of the received information from my
 * request class and do some operations with my database
 */
public class EventService {
    Database db;


    public EventService(){
        db = new Database();
    }
    /**
     * This functions do the operations for just a single events by making sure the events with the
     * given id are found and return that event
     * @param eventID event id of an event to be matched with event id's in database
     * @return
     * @throws DataAccessException
     */
    public Event performEventService(String eventID, String authTokens) throws DataAccessException, SQLException {
        Event event = null;
        try{
            db.startTransaction();
            EventDAO eventDAO = new EventDAO(db);
            event = eventDAO.find(eventID);
            if(event == null){
                db.closeConnection(true);
                return  null;
            }
            PersonDAO personDAO = new PersonDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            Person person = personDAO.find(event.getPersonID());
            AuthToken authToken = authTokenDAO.findByTokens(authTokens);
            if(event != null && (person.getAssociatedUserName().equals(
                    authToken.getAssociatedUsername()
            ))){
                db.closeConnection(true);
                return event;
            }
            event = null;
            db.closeConnection(true);
        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
        return event;
    }


    /**
     * This function performed the operations on multiple events in my event table by making sure the
     * auth tokens correspond to a user, and finding the users events in the database. It push all
     * found events in an array of event and returns the array
     * @param authTokens tokens of a user that will be matched with token in database
     * @return
     * @throws DataAccessException
     */
    public Event[] performEventsServiceOnMultipleEvents(String authTokens) throws DataAccessException, SQLException {
        ArrayList<Event> arrayList = new ArrayList<>();
        Event [] events = null;
        try {
            db.startTransaction();
            EventDAO eventDAO = new EventDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            PersonDAO personDAO = new PersonDAO(db);
            AuthToken authToken = authTokenDAO.findByTokens(authTokens);

            if(authToken == null){
                db.closeConnection(true);
                return  null;
            }
            if (authTokens != null) {
                arrayList = eventDAO.findByAssociatedUsername(authToken.getAssociatedUsername());
                events = new Event[arrayList.size()];
                events = arrayList.toArray(events);
            }
            if (arrayList.size() == 0) {
                db.closeConnection(true);
                return events;
            }
            db.closeConnection(true);
        } catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }

        return events;
       }
    }
