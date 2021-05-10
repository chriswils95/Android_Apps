package files.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.errorResponse.ErrorResponse;
import files.model.Person;
import files.result.EventsResult;
import files.result.LoginResult;
import files.result.PersonsResult;
import files.service.PersonService;
import files.success.Success;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;

/**
 * the Persons handler class implements the http handle for my person routes,
 * get make sure authentications is sets, and person id is sets and send data to my
 * person services for certain operations
 */
public class PersonHandler implements HttpHandler {
    static final String REQUEST_HEADER_KEY = "Authorization";
    static final String ERROR_MESSAGE = "error";
    static final String SPLIT_TOKEN = "/";
    static final String GET_ALL_PERSONS_URL = "person";
    static final int SPLIT_NUMBER = 2;
    static final int SET = 1;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers requestHeaders = exchange.getRequestHeaders();

        boolean success = false;
        ErrorResponse errorResponse = null;

        PersonsResult personsResult = null;
        EventsResult eventsResult = null;
        LoginResult loginResult = null;
        Success success1 = new Success();
        boolean isEvent = false;
        boolean isLogin = false;
        boolean isPerson = false;

        try {

        if (requestHeaders.containsKey(REQUEST_HEADER_KEY)) {

            // Extract the auth token from the "Authorization" header
            String authToken = requestHeaders.getFirst(REQUEST_HEADER_KEY);
            if(authToken.isEmpty()){
                success = false;
            }else {
                URI uri = exchange.getRequestURI();
                Person[] arraysOfPerson = null;
                String uriPath = uri.getPath();
                String splitedUri = uriPath;
                String personID = null;
                PersonService personService = new PersonService();
                String[] arrayOfSplitUri = splitedUri.split(SPLIT_TOKEN);
                String defaultHeader = arrayOfSplitUri[SET];
                if (defaultHeader.equals(GET_ALL_PERSONS_URL)
                        && arrayOfSplitUri.length == SPLIT_NUMBER) {
                    isPerson = true;
                    arraysOfPerson = personService.performServiceOnMuliplePerson(authToken);

                    personsResult = new PersonsResult(arraysOfPerson, true);

                    String respData = new Gson().toJson(personsResult);
                    // personsResult = new PersonsResult(arraysOfPerson);
                    if(personsResult.getData() == null){
                        success = false;
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);
                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete/
                        exchange.getResponseBody().close();
                        success = true;
                    }
                } else if (defaultHeader.equals(GET_ALL_PERSONS_URL) &&
                        arrayOfSplitUri.length > SPLIT_NUMBER) {
                    isPerson = true;
                    Headers reqHeaders = exchange.getRequestHeaders();
                    personService = new PersonService();
                    personID = arrayOfSplitUri[SPLIT_NUMBER];
                    Person person = personService.performPersonService(personID, authToken);
                    if(person == null){
                        success = false;
                    }
                    else {
                        personsResult = new PersonsResult(person.getPersonID(), person.getAssociatedUserName(),
                                person.getFirstName(), person.getLastName(),
                                person.getGender(), true);
                        personsResult.setFatherID(person.getFatherID());
                        personsResult.setMotherID(person.getMotherID());
                        personsResult.setSpouseID(person.getSpouseID());

                        String respData = new Gson().toJson(person);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);
                        // Close the output stream.  This is how Java knows we are done
                        // sending data and the response is complete/
                        exchange.getResponseBody().close();
                        success = true;
                    }
                }
            }
        }

            if (!success) {
                personsResult = new PersonsResult(ERROR_MESSAGE, success);
                String error1 = new Gson().toJson(personsResult);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(error1, respBody);

                // We are not sending a response body, so close the response body
                exchange.getResponseBody().close();
                //respBody.close();
            }
    }
        catch (IOException | DataAccessException | SQLException e ) {
            e.printStackTrace();
        }

    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
