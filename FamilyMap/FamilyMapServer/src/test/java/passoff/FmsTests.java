package passoff;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import files.model.AuthToken;
import files.model.Event;
import files.model.Person;
import files.model.User;
import files.request.LoadRequest;
import files.request.LoginRequest;
import files.request.RegisterRequest;
import files.result.FillResult;
import files.result.LoadResult;
import files.result.LoginResult;
import files.result.RegisterResult;
import files.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;
import files.dao.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import result.ClearResult;
import result.EventResult;
import result.PersonResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.channels.ScatteringByteChannel;
import java.sql.SQLException;
import java.util.ArrayList;

//We will use this to test that our insert method is working and failing in the right ways
public class FmsTests {
    private static Database db = new Database();
    ClearService clearService;
    private static RegisterResult registerResult;

    @Before
    public void setUp() throws DataAccessException, SQLException {
      clearService = new ClearService();
     clearService.performClearService();

    }


    @AfterAll
    static void tearDown() throws DataAccessException, SQLException {
        try{
            db.startTransaction();
            db.clearTables("Events");
            db.clearTables("Persons");
            db.clearTables("Users");
            db.clearTables("AuthTokens");
            db.closeConnection(true);

        }
        catch (DataAccessException | SQLException ex) {
            db.closeConnection(false);
        }
    }



    @Test
    public void insertNewUserPass() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("cw2636", "Isatu1997","chris",
                "wilson", "cw2636",
                "M", "chrissy");
        registerResult = null;
        try {
            RegisterService registerService = new RegisterService();

            registerResult = registerService.performRegisterServices(registerRequest);
        }catch (Exception e){

        }

        Assert.assertEquals(registerResult.getSuccess(), true);


        //While insert returns a bool we can't use that to verify that our function actually worked

    }

    @Test
    public void insertSameUserFail() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("cw2636", "Isatu1997","chris",
                "wilson", "cw2636",
                "M", "chrissy");
        RegisterResult registerResult = null;
        UserDAO userDAO = null;
        try {
            RegisterService registerService = new RegisterService();

            registerResult = registerService.performRegisterServices(registerRequest);
        }catch (Exception e){

        }

        Assert.assertEquals(registerResult, null);
    }

    @Test
    public void aloginUserPass(){
        LoginRequest loginRequest = new LoginRequest("cw2636", "Isatu1997");
        LoginResult loginResult = null;
        LoginService loginService = new LoginService();
        try {
            loginResult = loginService.performLoginServices(loginRequest);
        }catch (Exception e){

        }

        Assert.assertEquals(loginResult.getSuccess(), true);

    }

    @Test
    public void aloginUserFail(){
        LoginRequest loginRequest = new LoginRequest("james", "Is");
        LoginResult loginResult = null;
        LoginService loginService = new LoginService();
        try {
            loginResult = loginService.performLoginServices(loginRequest);
        }catch (Exception e){

        }

        Assert.assertEquals(loginResult, null);
    }

    @Test
    public void clearUserPass(){
        ClearService clearService = new ClearService();
        try {
            clearService.performClearService();
        }catch (Exception e){
         e.printStackTrace();
        }
    }

    @Test
    public void ClearUserFail(){
        ClearService clearService = null;
        try{
            clearService.performClearService();
        }catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertNull(clearService);
    }


    @Test
    public void loadUserPass() throws FileNotFoundException, DataAccessException, SQLException {
        String result = "";
        String testResult = "Successfully added 2 users, 11 persons, and 19 events to the database.";
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = new Gson().fromJson(jsonReader, LoadRequest.class);
            Event[] events = loadRequest.getEvents();
            User[] users = loadRequest.getUsers();
            Person[] persons = loadRequest.getPersons();

            LoadService loadService = new LoadService();
            loadService.loadDataIntoDatabase(events, persons, users);
            result = loadService.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertEquals(result, testResult);
    }


    @Test
    public void loadUserFail(){
        String result = "";
        String testResult = "";
        try {
            Event[] events = null;
            User[] users = null;
            Person[] persons = null;

            LoadService loadService = new LoadService();
            loadService.loadDataIntoDatabase(events, persons, users);
            result = loadService.toString();
        }catch (Exception e){
           e.printStackTrace();
        }

        Assert.assertEquals(result, testResult);
    }


    @Test
    public void getAllUserEventsPass(){
        EventService eventService = new EventService();
        Event[] events = null;
        try {
            events = eventService.performEventsServiceOnMultipleEvents(registerResult.getAuthToken());
        }catch(Exception e){

        }

        Assert.assertEquals(registerResult.getUserName(), events[0].getUsername());
    }

    @Test
    public void getAllEventsFail(){
        EventService eventService = new EventService();
        Event[] events = null;
        try {
            events = eventService.performEventsServiceOnMultipleEvents("");

        }catch (Exception e){
         e.printStackTrace();
        }

        Assert.assertNull(events);
    }

    @Test
    public void PersonService(){
        PersonService personService = new PersonService();
        Person personResult = null;
        String personID = "Sheila_Parker";
        try{
           personResult =  personService.performPersonService(registerResult.getPersonID(), registerResult.getAuthToken());
        }catch (Exception e){

        }

        Assert.assertNotNull(personResult);

    }

    @Test
    public void PersonServiceFail(){
        PersonService personService = new PersonService();
        Person personResult = null;
        String personID = "Sheila_Parker";
        try{
            personResult =  personService.performPersonService(personID, registerResult.getAuthToken());
        }catch (Exception e){

        }

        Assert.assertNull(personResult);
    }



    @Test
    public void PersonEvent() throws DataAccessException, SQLException {
        EventService eventService = new EventService();
        Event eventResult = null;
        String eventID = "Other_Asteroids";
        Database dbTest = new Database();
        dbTest.startTransaction();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(dbTest);
        AuthToken authToken = authTokenDAO.find("sheila");
        dbTest.closeConnection(true);
        try{
            eventResult = eventService.performEventService(eventID, authToken.getUniqueKeys());
        }catch (Exception e){

        }

        Assert.assertEquals(eventResult.getUsername(), authToken.getAssociatedUsername());
    }
    @Test
    public void PersonEventFail() throws DataAccessException, SQLException {
        EventService eventService = new EventService();
        Event eventResult = null;
        String eventID = "Other_Asteroids";
        Database dbTest = new Database();
        dbTest.startTransaction();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(dbTest);
        AuthToken authToken = authTokenDAO.find("");
        dbTest.closeConnection(true);
        try{
            eventResult = eventService.performEventService(eventID, authToken.getUniqueKeys());
        }catch (Exception e){

        }

        Assert.assertNull(eventResult);
    }


    @Test
    public void getAllPersons(){
        PersonService personService = new PersonService();
        Person[] people = null;
        try{
           people = personService.performServiceOnMuliplePerson(registerResult.getAuthToken());

        }catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertEquals(people[0].getAssociatedUserName(), registerResult.getUserName());
    }

    @Test
    public void getAllPersonsFail(){
        PersonService personService = new PersonService();
        Person[] people = null;
        try{
            people = personService.performServiceOnMuliplePerson(registerResult.getAuthToken());

        }catch (Exception e){
           e.printStackTrace();
        }

        Assert.assertNull(people);
    }


    @Test
    public void fillTestPass(){

        FillService fillService = new FillService();
        String fillResult = null;
        String testResult = "Successfully added 75 persons and 208 events to the database.";

        try {
            fillResult = fillService.performedFilledService("cw2636", 5);
        }catch (Exception e){
           e.printStackTrace();
        }

        Assert.assertEquals(fillResult, testResult);

    }

    @Test
    public void fillTestFail(){
        FillService fillService = new FillService();
        String fillResult = null;

        try {
            fillResult = fillService.performedFilledService("James", 1);
        }catch (Exception e){
          e.printStackTrace();
        }

        Assert.assertNull(fillResult);
    }

    @Test
    public void testInsertUserFail() throws DataAccessException, SQLException {
        //if we call the method the first time it will insert it successfully
        Database udb = new Database();
        udb.startTransaction();
        UserDAO userDAO = new UserDAO(udb);
        User user = null;
        assertThrows(NullPointerException.class, ()-> userDAO.insert(user));
        udb.closeConnection(true);
    }



    @Test
    public void insertEvents() throws DataAccessException, SQLException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        Database edb = new Database();
        edb.startTransaction();
        Event bestEvent = new Event("james","james", "jal", 82.5f, 106.9f,
                "Usa", "africa","birth", 1999);
        EventDAO eventDAO = new EventDAO(edb);
        eventDAO.insert(bestEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eventDAO.find(bestEvent.getEventID());
        edb.closeConnection(true);
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assert.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        Assert.assertEquals(bestEvent.getPersonID(), compareTest.getPersonID());
    }


    @Test
    public void insertPersonFail() throws DataAccessException, SQLException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        Database pdb = new Database();
        pdb.startTransaction();
        PersonDAO personDAO = new PersonDAO(pdb);
        Person person = null;
        assertThrows(NullPointerException.class, ()-> personDAO.insert(person));
        pdb.closeConnection(true);


    }


    @Test
    public void insertPerson() throws DataAccessException, SQLException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        Database pdb = new Database();
        pdb.startTransaction();
        PersonDAO personDAO = new PersonDAO(pdb);
        Person person = new Person("james1", "cw", "John", "joe",
                "M");
        Person compareTest = null;
        try {

            personDAO.insert(person);
            //So lets use a find method to get the event that we just put in back out
            compareTest = personDAO.find(person.getPersonID());
            pdb.closeConnection(true);
        }catch (Exception e){

        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assert.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        Assert.assertEquals(person.getPersonID(), compareTest.getPersonID());
    }


    @Test
    public void insertUserFail() throws DataAccessException, SQLException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        Database pdb = new Database();
        pdb.startTransaction();
        UserDAO userDAO = new UserDAO(pdb);
        User user = null;
        assertThrows(NullPointerException.class, ()-> userDAO.insert(user));
        pdb.closeConnection(true);


    }


    @Test
    public void insertUser() throws DataAccessException, SQLException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        Database udb = new Database();
        udb.startTransaction();
        UserDAO userDAO = new UserDAO(udb);
        User user = new User("D1234", "LO", "Kris", "willy",
                "ls@email", "M", "blank");
               User compareTest = null;
        try {

            userDAO.insert(user);
            //So lets use a find method to get the event that we just put in back out
            compareTest = userDAO.find(user.getUsername());
            udb.closeConnection(true);
        }catch (Exception e){

        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assert.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        Assert.assertEquals(user.getPersonID(), compareTest.getPersonID());
    }

    @Test
    public void insertAuthTokenFail() throws DataAccessException, SQLException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        Database adb = new Database();
        adb.startTransaction();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(adb);
        AuthToken authToken = null;
        assertThrows(NullPointerException.class, ()-> authTokenDAO.insert(authToken));
        adb.closeConnection(true);


    }


    @Test
    public void insertAuthtoken() throws DataAccessException, SQLException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        Database adb = new Database();
        adb.startTransaction();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(adb);
        AuthToken authToken = new AuthToken("trish", "1234");
        AuthToken compareTest = null;
        try {

            authTokenDAO.insert(authToken);
            //So lets use a find method to get the event that we just put in back out
            compareTest = authTokenDAO.find(authToken.getAssociatedUsername());
            adb.closeConnection(true);
        }catch (Exception e){

        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        Assert.assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        Assert.assertEquals(authToken.getAssociatedUsername(), compareTest.getAssociatedUsername());
    }








}

