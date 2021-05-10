package files.handler;


import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.errorResponse.ErrorResponse;
import files.model.Event;
import files.model.Person;
import files.result.EventsResult;
import files.service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;

/**
 * The events handler handlers the http handler for my events routes
 * It checks for error messages, bad address, parse request body and send the info to my events services
 * and make sure user authentication is sets
 */



public class EventHandler implements HttpHandler {

    static final String REQUEST_TYPE = "get";
    static final String REQUEST_HEADER_KEY = "Authorization";
    static final String EVENTS_URL = "event";
    static final String SPLIT_TOKEN = "/";
    /**
     * This handle function which implements the http handler just handles my events routes
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {


        boolean success = false;
        ErrorResponse errorResponse = null;
        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            EventsResult eventsResult = null;

            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {

                Headers requestHeaders = exchange.getRequestHeaders();
                URI uri = exchange.getRequestURI();
                Person[] arraysOfPerson = null;
                String uriPath = uri.getPath();
                String splitedUri = uriPath;
                String personID = null;
                String[] arrayOfSplitUri = splitedUri.split(SPLIT_TOKEN);
                String defaultHeader = arrayOfSplitUri[1];
                if (requestHeaders.containsKey(REQUEST_HEADER_KEY)) {

                    // Extract the auth token from the "Authorization" header
                    String authToken = requestHeaders.getFirst(REQUEST_HEADER_KEY);
                    if (authToken.isEmpty()) {
                        success = false;
                    }
                    if (defaultHeader.equals(EVENTS_URL)) {
                        EventService eventService = new EventService();
                        String eventID = null;
                        Event event = null;
                        Event[] arraysOfEvents = null;
                        String respData = null;
                        if (arrayOfSplitUri.length > 2) {
                            eventID = arrayOfSplitUri[2];
                        }
                        if (eventID != null) {
                            try {
                                event = eventService.performEventService(eventID, authToken);
                            } catch (DataAccessException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (event == null) {
                                success = false;
                            } else {
                                eventsResult = new EventsResult(event.getEventID(), event.getUsername(),
                                        event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry()
                                        , event.getCity(), event.getEventType(), event.getYear(), true);
                                respData = new Gson().toJson(eventsResult);
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                OutputStream respBody = exchange.getResponseBody();
                                // Write the JSON string to the output stream.
                                writeString(respData, respBody);
                                // Close the output stream.  This is how Java knows we are done
                                // sending data and the response is complete/
                                exchange.getResponseBody().close();
                                success = true;
                            }

                        } else {
                            arraysOfEvents = eventService.performEventsServiceOnMultipleEvents(authToken);

                            if(arraysOfEvents == null){
                                success = false;
                            }
                            else {
                                eventsResult = new EventsResult(arraysOfEvents, true);
                                respData = new Gson().toJson(eventsResult);
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

            }

            if (!success) {
                eventsResult = new EventsResult("error", success);
                String error  = new Gson().toJson(eventsResult);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(error, respBody);
                // We are not sending a response body, so close the response body
                exchange.getResponseBody().close();
                //respBody.close();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
          /*  catch (DataAccessException | SQLException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.


            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            OutputStream respBody = exchange.getResponseBody();

            String errorResponseBody = e.toString();
            errorResponse = new ErrorResponse(errorResponseBody, success);
            // Write the JSON string to the output stream.
            String j = new Gson().toJson(errorResponse);
            writeString(j, respBody);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            //respBody.close();
            exchange.getResponseBody().close();


            // Display/log the stack trace
            e.printStackTrace();
        }*/
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
