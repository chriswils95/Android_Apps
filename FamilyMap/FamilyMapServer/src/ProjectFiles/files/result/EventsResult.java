package files.result;


import files.model.Event;
import files.success.Success;

/**
 *The Events results class sets the results for either an array of events if the routes
 * ask for an array or the corresponding events infos. It also extends the success super class to
 * include the success message
 */
public class EventsResult extends Success {
    /**
     * array of all events of a user
     */
    Event [] data;
    /** id of an event of user
     *
     */
    private String eventID;
    /**
     * username of a user
     */
    private String associatedUsername;
    /**
     * id of a user
     */
    private String personID;
    /**
     * latitude of an event location
     */
    private float latitude;
    /**
     * longitude of an event location
     */
    private float longitude;
    /**
     * country of an event location
     */
    private String country;
    /**
     * city of an event
     */
    private String city;
    /**
     * types of user events
     */
    private String eventType;
    /**
     * year an event occurred
     */
    private int year;


    /**
     * This constructors sets the given variables for a single person events
     * and also sets the success message
     * @param eventID id of an event
     * @param username username of the current user
     * @param personID id of the user
     * @param latitude latitude of user event locations
     * @param longitude logitude of user events locations
     * @param country country an event occurred
     * @param city city an event occurred
     * @param eventType types of an event
     * @param year year an event occurred
     * @param success success message if an event was found
     */
    public EventsResult(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year,
                        boolean success) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        super.setSuccess(success);

    }

    /**
     * This constructor sets the events for an array of events and also sets the success message
     * @param events arrays of events of a given user
     * @param success success message if events are found
     */
    public EventsResult(Event [] events, boolean success){
        this.data = events;
         super.setSuccess(success);
    }

    public  EventsResult(String message, boolean success){
        super.setMessage(message);
        super.setSuccess(success);
    }


}
