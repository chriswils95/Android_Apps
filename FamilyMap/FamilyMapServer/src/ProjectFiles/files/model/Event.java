//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


package files.model;
/**
 * The Event model class specifies the functionality that makes a user
 * like all necessary information needed for a user to be qualified as a event
 * It uses getters and setters and even parameterized constructors to set these variable
 */
public class Event {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;



    /**
     *  The event constructors sets a user with its given information. It also sets the success message by
     *      *   extending the success super class
     * @param eventID
     * @param username
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }


    /**
     * The eventData sub class are classes that will help me in parsing
     *    the json file into an array of persons
     */
    public class EventData{
        /**
         * The events variable is an array of events of the given person
         */
        Event [] events;


        /**
         * returns the size of my events array
         * @return
         */
        public int eventsSize(){
            return events.length;
        }

        /**
         * returns an event object at a specific index of my event array
         * @param index index of an event in my event array
         * @return
         */
        public Event getValueAtIndex(int index){
            return events[index];
        }
    }

    /**
     * This function returns the event id
     * @return event id
     */
    public String getEventID() {
        return this.eventID;
    }


    /**
     * This function sets the event id
     * @param eventID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * This function gets the username
     * @return associated username
     */
    public String getUsername() {
        return this.associatedUsername;
    }

    /**
     * This function sets the username
     * @param username
     */
    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    /**
     * This function returns the person id
     * @return person id
     */
    public String getPersonID() {
        return this.personID;
    }

    /**
     * This function sets the person id
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * This fucntion gets the latitude
     * @return latitude
     */
    public float getLatitude() {
        return this.latitude;
    }

    /**
     * This function sets the latitude
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * This function returns the longitude
     * @return longitude
     */
    public float getLongitude() {
        return this.longitude;
    }

    /**
     * This function sets the longitude
     * @param longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * This function returns the country
     * @return country
     */
    public String getCountry() {
        return this.country;
    }


    /**
     * This function sets the country
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * This function returns the city
     * @return city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * This function sets the city with given city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * This function returns the event type
     * @return event type
     */
    public String getEventType() {
        return this.eventType;
    }

    /**
     * This function sets the event type
     * @param eventType
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * This function returns the year
     * @return year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * This function sets the year with the given year
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

}
