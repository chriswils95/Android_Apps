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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import files.model.Event;
import files.model.Person;

/**
 * The eventDAO Class implements all necessary data access operations to my event table
 * in the table. It uses a secured connection to do all the necessary operations
 */
public class EventDAO {
    /**
     * This field is just my database connection which will be set in my constructor
     */
    private  HashMap<Person, Integer> people;
    Database db;


    /**
     * This constructor sets a the given connection to my connection, which will be used in
     * other data access operation
     *      *                   connections with my database
     */

    public EventDAO(Database db) {
        this.db = db;
    }


    /**
     * The insert function inserts a given events to my database events table
     * It throws an error if the connection is bad or the events table is not found
     * or was not created
     * @param event The events of a given user to be insert into the event table
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {
        try {
            String query = "INSERT INTO EVENTS VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = db.conn.prepareStatement(query);
            stmt.setObject(1, event.getEventID());
            stmt.setObject(2, event.getUsername());
            stmt.setObject(3, event.getPersonID());
            stmt.setObject(4, event.getLatitude());
            stmt.setObject(5, event.getLongitude());
            stmt.setObject(6, event.getCountry());
            stmt.setObject(7, event.getCity());
            stmt.setObject(8, event.getEventType());
            stmt.setObject(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException var4) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * The findByAssociatedUsername function find the given associated username in my events table
     * and returns all the events that correspond to that associated username, it returns null if the username wasnt found, and
     * throws an error if there was bad connections or problems with the database
     * @param associatedUsername given username to be matched with usernames in database
     * @return array of events that correspond with the given user
     * @throws DataAccessException
     * @throws SQLException
     */
    public ArrayList<Event> findByAssociatedUsername(String associatedUsername) throws DataAccessException, SQLException {
        ArrayList<Event> arrayOfEvents = new ArrayList<>();
        int index = 0;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                arrayOfEvents.add(event);

            }

            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return arrayOfEvents;
    }


    /**
     * The find function acts the same way as the findByAssociated Username function. The only difference
     * is that this function find a given events that corresponds to the given eventId and returns just that
     * events. EventID is unique in the events table. It returns null if the eventID doesnt match with any in my
     * events table and throws an error if the connection fails or problems with my database
     * @param eventID The ID of an event to be used to matched with ID's in my event table
     * @return the events that correspond with the given event id
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        ResultSet rs = null;
        Event event = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try{
            PreparedStatement stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));

            }

            rs.close();
            stmt.close();
        } catch (Exception e)

        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return event;
    }


    /**
     * The deleteUserFromDatabase functions deletes a given user events in my events table that corresponds to the
     * given username, if user not in database it prints out an error message, and if there was an error
     * like connections, or database access failures it throws it
     *                   connections with my database
     * @param username The username will be used to see if it corresponds to a user in my table
     *                 and delete that user
     */
    public void deleteUserEventsFromDatabase(String username) {
        String deleteUser = "DELETE FROM EVENTS WHERE ASSOCIATEDUSERNAME LIKE '" + username + "'";

        try {
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery(deleteUser);
            if (!rs.next()) {
                System.out.println("Event is not found, please register user");
            } else {
                System.out.println("Events successful deleted");
            }
        } catch (SQLException var6) {
            System.out.println(var6.getMessage());
        }

    }

    /**
     * The findNumOfEventsInEventTable just returns the number of events in my event table by looping through
     * each events and connection them. It throws an error if the connection is corrupt events table not created
     * or data access erroes
     *      *                   connections with my database
     * @param size This is just the size of my events table
     * @return The number of events in the table
     */

    public int findNumOfEventsInEventTable(int size) {
        String sql = "SELECT * FROM EVENTS";

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
        return  size;
    }

    public ArrayList<Event> getEventsTable(){
        String sql = "SELECT * FROM EVENTS";

        ArrayList<Event> eventsTable = new ArrayList<Event>();
        try {
            Statement stmt = db.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                Event event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));

                eventsTable.add(event);
            }
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        }
        return eventsTable;
    }

}
