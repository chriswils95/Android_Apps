package files.service;
import com.google.gson.Gson;
import files.dao.*;
import files.model.AuthToken;
import files.model.Event;
import files.model.Person;
import files.model.User;
import files.parsingJsonFiles.JsonArrayParser;
import files.parsingJsonFiles.JsonArrayParser.LocationData;
import files.request.RegisterRequest;
import files.result.RegisterResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * The register service class performs the necessary operations of the received information from my
 * request class and clear all data in my database, I do all my logic for persons and events in this servive because i dont wanna
 * change or complicate things and a TA advised me not to change my code since it is up and working
 */
public class RegisterService {
    private ArrayList<String> arrayOfMaleNames;
    private ArrayList<String> arrayOfLastNames;
    private ArrayList<String> arrayOfFemaleNames;
    private JsonArrayParser.LocationData  arrayOfLocations;
    private JsonArrayParser jsonObjects;
    private Person personUser;
    int defaultNumOfGenerations;
    int[] arrayOfUniqueNum;
    Integer [] arr;
    static final String MALE = "m";
    static final String FEMALE = "f";
    static final String BIRTH_EVENT_TYPE = "birth";
    static final String DEATH_EVENT_TYPE = "Death";
    static final String MARRIAGE_EVENT_TYPE  = "Marriage";
    static final int DEFAULT_YEAR = 1997;
    static final int ARRAY_SIZE = 146;
    static final int MOTHER_GENERATION_COUNT = 30;
    static final int FATHER_GENERATION_COUNT = 31;
    static final int DEATH_AGE = 50;
    static final int MARRIAGE_AGE = 35;



    static int index = 0;
    Random rand;
    Database db;

    /**
     * This constructor initializes my variables
     */
    public RegisterService() throws FileNotFoundException {
        db = new Database();
        defaultNumOfGenerations = 4;
        jsonObjects = new JsonArrayParser();
        arrayOfMaleNames = jsonObjects.getArrayOfMaleNames();
        arrayOfFemaleNames = jsonObjects.getArrayOfFemaleNames();
        arrayOfLastNames = jsonObjects.getArrayOfLastNames();
        arrayOfLocations = jsonObjects.getArrayOfLocations();
        arr = new Integer[146];
        index = 0;
    }

    public RegisterService(Database db) throws FileNotFoundException {
        this.db = db;
        defaultNumOfGenerations = 4;
        jsonObjects = new JsonArrayParser();
        arrayOfMaleNames = jsonObjects.getArrayOfMaleNames();
        arrayOfFemaleNames = jsonObjects.getArrayOfFemaleNames();
        arrayOfLastNames = jsonObjects.getArrayOfLastNames();
        arrayOfLocations = jsonObjects.getArrayOfLocations();
        arr = new Integer[ARRAY_SIZE];
        index = 0;
    }


    /**
     * The performRegisterServices class uses the request class objects to check to see if the given
     * username corresponds to a person in the database, it returns null if person is present or generated
     * a person with a generation of persons and events of 4 if not presents
     * @param regRequest Register request objects that contains infos of the registered users
     * @return
     * @throws DataAccessException
     */
    public RegisterResult performRegisterServices(RegisterRequest regRequest) throws DataAccessException, SQLException {
        RegisterResult registerResult = null;
        String personID = UUID.randomUUID().toString();
        User user = new User(regRequest.getUserName(), regRequest.getPassword(), regRequest.getFirstName(),
                regRequest.getLastName(), regRequest.getEmail(), regRequest.getGender(), personID);
        String authTokenUniqueKeys = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(regRequest.getUserName(),authTokenUniqueKeys);
        Person person = null;
        try {
            // Open database connection
            db.startTransaction();
            UserDAO userDAO = new UserDAO(db);
            PersonDAO personDAO = new PersonDAO(db);
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db);
            EventDAO eventDAO = new EventDAO(db);
           if(userDAO.find(user.getUsername()) == null){
                userDAO.insert(user);
                authTokenDAO.insert(authToken);
                person = new Person(user.getPersonID(), user.getUsername(),
                        user.getFirstName(), user.getLastName(), user.getGender());
               fillPersonGenerations(person, defaultNumOfGenerations, personDAO, eventDAO);
               registerResult = new RegisterResult(authToken.getUniqueKeys(), user.getUsername(), personID,
                       true);

            }
            db.closeConnection(true);

       }
       catch (DataAccessException ex) {
            db.closeConnection(true);
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return registerResult;
    }

    /**
     * This function just creates a mother for the given person
     * @param personUser user to create mother for
     * @return created mother
     * @throws FileNotFoundException
     * @throws SQLException
     * @throws DataAccessException
     */
    private Person makeMother(Person personUser, int r) throws FileNotFoundException {

        //random generating a UUID string to be used for the mother id
       // String motherID = generateRandomAuthTokenString();
        // random assign female first name from my female first name array
       // r = rand.nextInt(146);

        if(index == ARRAY_SIZE){
            index = 0;
        }

        String motherFirstName = arrayOfFemaleNames.get(index);
        // random assign last name from my last name array
        String motherLastName = arrayOfLastNames.get(index); //arrayOfLastNames.getLastNameAt(r);



        //Making the mother
        Person mother = new Person();
        mother.setPersonID(generateRandomAuthTokenString());
        mother.setAssociatedUserName(personUser.getAssociatedUserName());
        mother.setFirstName(motherFirstName);
        mother.setLastName(motherLastName);
        mother.setGender(FEMALE);


        return mother;
    }


    /**
     * This function creates a father for the given persons
     * @param personUser user to create father for
     * @return created father
     * @throws FileNotFoundException
     * @throws SQLException
     * @throws DataAccessException
     */

    private Person makeFather(Person personUser, int r) throws FileNotFoundException {

        //random generating a UUID string to be used for the mother id
        String fatherID = generateRandomAuthTokenString();
        if(index == ARRAY_SIZE){
            index = 0;
        }
        index++;
        // random assign male first name from my male first name array
      //  r = rand.nextInt(146);
        String fatherFirstName = arrayOfMaleNames.get(index);
        // random assign last name from my last name array
        String fatherLastName = arrayOfLastNames.get(index);//arrayOfLastNames.getLastNameAt(r);

        //Making father
        Person father = new Person();
        father.setPersonID(fatherID);
        father.setAssociatedUserName(personUser.getAssociatedUserName());
        father.setFirstName(fatherFirstName);
        father.setLastName(fatherLastName);
        father.setGender(MALE);

        return father;
    }


    /**
     * This function generates birth events for a person by random assigning locations data
     * like city, country, longitude and latitude and used those to create an events
     * for the person and insert the created events into my events table in the database
     * @param personUser person will be creating events for
     * @param year events year
     * @param eventDAO instance of the eventDAO class used to insert events
     * @throws FileNotFoundException
     * @throws DataAccessException
     * @throws SQLException
     */
    private void createBirthEvents(Person personUser, int year, EventDAO eventDAO)
            throws FileNotFoundException, DataAccessException, SQLException {
        String eventID = generateRandomAuthTokenString();
        int r = rand.nextInt(arrayOfLocations.size());

        // create user birth events
        Event events = new Event(eventID, personUser.getAssociatedUserName(), personUser.getPersonID(),
                arrayOfLocations.getLatitude(r), arrayOfLocations.getLongitude(r), arrayOfLocations.getCountry(r),
                arrayOfLocations.getCity(r), BIRTH_EVENT_TYPE, year);

        eventDAO.insert(events);
    }

    /**
     * This function generates a random UUID authtoken string
     * @return random string
     */
    String generateRandomAuthTokenString(){
        return  UUID.randomUUID().toString();
    }

    /**
     * This function generates death events for a person by random assigning locations data
     * like city, country, longitude and latitude and used those to create an events
     * for the person and insert the created events into my events table in the database
     * @param personUser person will be creating events for
     * @param year year of events
     * @param eventDAO instance of the eventDAO class used to insert events
     * @throws DataAccessException
     * @throws FileNotFoundException
     * @throws SQLException
     */
    private void createDeathEvents(Person personUser, int year, EventDAO eventDAO) throws DataAccessException, FileNotFoundException, SQLException {

        year += DEATH_AGE;
        String deathEventID = generateRandomAuthTokenString();
        Random randDeathLocationArrayIndex = new Random();
        int randValue = randDeathLocationArrayIndex.nextInt(arrayOfLocations.size());
        Event deathEvents = new Event(deathEventID, personUser.getAssociatedUserName(), personUser.getPersonID(),
                arrayOfLocations.getLatitude(randValue), arrayOfLocations.getLongitude(randValue),
                arrayOfLocations.getCity(randValue),
                arrayOfLocations.getCountry(randValue), DEATH_EVENT_TYPE, year);

        eventDAO.insert(deathEvents);
    }


    /**
     * This create marriage events for the current person, it generates a random value which is used to access my
     * locations arrays and used the random city, country, latitude and longitude to create the events
     * @param person
     * @param arrayOfLocations
     * @param year
     * @param randVal
     * @param eventDAO
     * @throws FileNotFoundException
     * @throws DataAccessException
     * @throws SQLException
     */
    private void createMarriageEvents(Person person, LocationData arrayOfLocations,
                                      int year, int randVal, EventDAO eventDAO) throws FileNotFoundException, DataAccessException, SQLException {
        // Marriage for both Father and Mother
        year += MARRIAGE_AGE;
        String marraigeEventID = UUID.randomUUID().toString();
        Event marriageEvents = new Event(marraigeEventID, person.getAssociatedUserName(), person.getPersonID(),
                arrayOfLocations.getLatitude(randVal), arrayOfLocations.getLongitude(randVal),
                arrayOfLocations.getCity(randVal),
                arrayOfLocations.getCountry(randVal), MARRIAGE_EVENT_TYPE, year);

        eventDAO.insert(marriageEvents);
    }
    public Map<Integer, List<Person>> groupDuplicateYear(Map<Person, Integer> temp){

        Map<Integer, List<Person>> valueMap = temp.keySet().stream().collect(Collectors.
                groupingBy(k -> temp.get(k)));

        return valueMap;
    }


    /**
     * This function creates marraige events for parents or spouses, it makes the marriage events for both
     * parents are the same
     * @param personDAO
     * @param eventDAO
     * @throws FileNotFoundException
     * @throws DataAccessException
     * @throws SQLException
     */
    private void createMarriageEventsForParent(PersonDAO personDAO, EventDAO eventDAO) throws
            FileNotFoundException, DataAccessException, SQLException {
        Map<Integer, List<Person>> mapOfDuplicatesMarriageYears = new TreeMap<Integer, List<Person>>();
        Random randMarriageLocationArrayIndex = new Random();
        int randVal = 0;
        int year = 0;
        Map<Person, Integer> people = findSpouses(personDAO, eventDAO);
        mapOfDuplicatesMarriageYears = groupDuplicateYear(people);
        randVal = randMarriageLocationArrayIndex.nextInt(arrayOfLocations.size());
        createMarriageEvents(personUser, arrayOfLocations, 1997 + year, randVal, eventDAO);
        if(people != null){
            int i = 0;
            for(Map.Entry<Integer,List<Person>> entry : mapOfDuplicatesMarriageYears.entrySet()) {
                year = entry.getKey();

                List<Person> marriageCouples = entry.getValue();

                Iterator it = marriageCouples.iterator();

                while (it.hasNext()){
                    createMarriageEvents((Person)it.next(),arrayOfLocations, year, randVal, eventDAO);
                }
                randMarriageLocationArrayIndex = new Random();
                randVal = randMarriageLocationArrayIndex.nextInt(arrayOfLocations.size());
            }
        }
    }


    /**
     * This functions loop through my persons table and find all spouses and put it in a map
     * with their corresponding years
     * @param personDAO
     * @param eventDAO
     * @return a map of spouses and their years
     * @throws DataAccessException
     * @throws SQLException
     */
    private Map<Person, Integer> findSpouses(PersonDAO personDAO, EventDAO eventDAO) throws DataAccessException, SQLException {
        ArrayList<String> arraysOfSpouse = new ArrayList<String>();
        HashMap<Person, Integer> people;
        ArrayList<Event> events = eventDAO.getEventsTable();
        people = new HashMap<Person, Integer>();
        String personID = null;
        Person person = null;
        Person personUser = null;


        // looking through the events and find the person with the events and its spouse
        for (int i = 0; i < events.size(); i++) {
            personID = events.get(i).getPersonID();
            person = personDAO.findPersonsBySpouseID(personID);
            if (events.get(i).getEventType().equals(BIRTH_EVENT_TYPE) && person != null) {
                personUser = personDAO.find(events.get(i).getPersonID());
                arraysOfSpouse.add(person.getPersonID());
                if (person != null) {
                    int temp = 0;
                    int index = arraysOfSpouse.indexOf(personUser.getPersonID());
                    if (index < 0) {
                        temp = events.get(i).getYear();
                        people.put(personUser, temp);
                    } else {
                        String tempId = arraysOfSpouse.get(index);
                        Person person2 = personDAO.findPersonsBySpouseID(tempId);
                        temp = getYear(people, person2);
                        people.put(personUser, temp);
                    }
                }
            }

        }
        return people;
    }


    /**
     * This function returns the year of the corresponding person in the map
     * @param temp
     * @param person
     * @return year of the person or -1 for error
     */
    private int getYear(HashMap<Person, Integer> temp, Person person){
        for(Map.Entry<Person, Integer> entry : temp.entrySet()){
            if(entry.getKey().equals(person)){
                return entry.getValue();
            }
        }
        return -1;
    }


    /**
     * This function generates events and persons for the given number of generations
     * It links mother and father and person to their respective family
     * @param personUser current user
     * @param generationsCount user geneations to be generated
     * @param year current birth year of user
     * @param r a random value between 0 to 146
     * @param personDAO  instance of the personDAO class
     * @param eventDAO instance of the personDAO class
     * @throws FileNotFoundException
     * @throws DataAccessException
     * @throws SQLException
     */
    private void populatePersonsAndEventsTable(Person personUser, int generationsCount, int year
            , int r, PersonDAO personDAO, EventDAO eventDAO)
            throws FileNotFoundException, DataAccessException, SQLException {


        // r = rand.nextInt(146);
        // recursively generates mother for current user
        Person mother = makeMother(personUser, r);

       // r = personUser.getAssociatedUserName().charAt(0) + generationsCount;
        // recursively generates mfater for current user
        Person father = makeFather(personUser, r);

      //  r = personUser.getAssociatedUserName().charAt(2) + generationsCount;


        // link mother and father spouse id
        mother.setSpouseID(father.getPersonID());
        father.setSpouseID(mother.getPersonID());




        if(generationsCount > 0){
            // link user to parents
            personUser.setFatherID(father.getPersonID());
            personUser.setMotherID(mother.getPersonID());
        }


        //add user to the database and generate birth and death events
        personDAO.insert(personUser);
        createDeathEvents(personUser,year, eventDAO);
        createBirthEvents(personUser,year,eventDAO);


        // recursively generate events until the geneation count is zero
        if(generationsCount > 0){
            generationsCount--;
            populatePersonsAndEventsTable(mother, generationsCount, year - MOTHER_GENERATION_COUNT, r,
                    personDAO, eventDAO);
            populatePersonsAndEventsTable(father, generationsCount, year - FATHER_GENERATION_COUNT, r,
                    personDAO, eventDAO);
        }

    }


    /**
     * The fill person generation class generates the family tree for a user, and populates the events
     * table with the actual exams for users and generations, it uses the gson to parse my json arrays
     * and uses the arrays to generates names, and locations
     * @param person
     * @param generations
     * @param personDAO
     * @param eventDAO
     * @throws FileNotFoundException
     * @throws DataAccessException
     * @throws SQLException
     */
    public void fillPersonGenerations(Person person, int generations, PersonDAO personDAO, EventDAO eventDAO)
            throws FileNotFoundException, DataAccessException, SQLException {
             rand = new Random();
             int size = 0;
             this.personUser= person;
            arrayOfUniqueNum = rand.ints(ARRAY_SIZE, 0,ARRAY_SIZE).toArray();
            size = arrayOfUniqueNum.length;
             // create events for the users and generate the family tree
        populatePersonsAndEventsTable(person, generations, DEFAULT_YEAR, size, personDAO, eventDAO);
            // loop through each user and find the parents and generates events for spouses
           createMarriageEventsForParent(personDAO, eventDAO);
    }



}
