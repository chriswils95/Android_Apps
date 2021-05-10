

package files.request;

import files.model.Event;
import files.model.Person;
import files.model.User;
/**
 * The Load Request stores the array of parse json strings in the specific model objects
 * like event, user and person.
 * It will be used in my service class
 */
public class LoadRequest {
    /**
     * arrays of all events that correspond to a user
     */
    private Event[] events;
    /**
     * arrays of all family members of a user
     */
    private Person [] persons;
    /**
     * array of all users
     */
    private User [] users;


    /**
     * This function returns the array of events
     * @return array of events
     */
    public Event[] getEvents() {
        return events;
    }

    /**
     * This function sets the array of events
     * @param events
     */
    public void setEvents(Event[] events) {
        this.events = events;
    }

    /**
     * This function returns the array of people
     * @return array of persons
     */
    public Person[] getPersons() {
        return persons;
    }

    /**
     * This function sets the array of persons
     * @param persons
     */
    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    /**
     * This function gets the array of uers
     * @return array of users
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * This function sets the array of users
     * @param users
     */
    public void setUsers(User[] users) {
        this.users = users;
    }

    /**
     * The LoadRequest constructor sets the parse json arrays of objects into the necessary
     * json objects
     * @param events arrays of events
     * @param persons arrays of family members of users
     * @param users arrays of users
     */

    /**
     *
     * @param eventData
     * @param personData
     * @param userData
     */
    public LoadRequest(Event.EventData  eventData, Person.PersonData personData,
                       User.UserData userData){
        events = new Event[eventData.eventsSize()];
        persons = new Person[personData.personsSize()];
        users = new User[userData.usersSize()];
       for(int i = 0; i < eventData.eventsSize(); i++){
           events[i] = eventData.getValueAtIndex(i);
       }

        for(int i = 0; i < personData.personsSize(); i++){
            persons[i] = personData.personAtIndex(i);
        }
        for(int i = 0; i < userData.usersSize(); i++){
            users[i] = userData.userAtIndex(i);
        }
    }
}
