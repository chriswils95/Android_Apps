/**
 * The clear handler class implements the http handler tasks for both the clear routes, events
 * and persons routes.
 */

package files.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.errorResponse.ErrorResponse;
import files.model.Event;
import files.model.Person;
import files.model.User;
import files.request.LoadRequest;
import files.result.ClearResult;
import files.result.LoadResult;
import files.result.RegisterResult;
import files.service.ClearService;
import files.success.Success;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;


/**
 * The clear handler Implements http handler for my /clear routes it make sure that there is no bad
 * address and
 * parse the necessary exchange body from json to a string to be used in my request and services classes
 */
public class ClearHandler implements HttpHandler {

    static final String REQUEST_TYPE = "post";
    static String SUCCESS_MESSAGE = "Clear succeeded";
    static String PATH = "/clear";
    static String ERROR_MESSAGE = "error";
    /**
     * The handle function implements the http functionlaity of the clear routes like
     * checking for bad address and doing the clear functionality in database
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // However, it does demonstrate the following:
        // 1. How to post the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to read JSON data from the HTTP request body
        // 4. How to return the desired status code (200, 404, etc.)
        //		in an HTTP response
        // 5. How to check an incoming HTTP request for an auth token

        boolean success = false;
        ErrorResponse errorResponse = null;
        ClearResult clearResult = null;
        LoadResult loadResult = null;
        Success success1 = new Success();
        LoadRequest loadRequest = null;

        try {


            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {
                URI uri = exchange.getRequestURI();
                String uriPath = uri.getPath();
                String respData = null;
                ClearService clearService = new ClearService();
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                // Read JSON string from the input stream
                String reqData = readString(reqBody);
                    Person.PersonData personData = new Gson().fromJson(reqData, Person.PersonData.class);
                Event.EventData eventData = new Gson().fromJson(reqData, Event.EventData.class);
                User.UserData userData = new Gson().fromJson(reqData, User.UserData.class);
                clearService.performClearService();
                if(uriPath.equals(PATH)){
                    respData = SUCCESS_MESSAGE;
                   // success1.setSuccess(true);
                    clearResult = new ClearResult(SUCCESS_MESSAGE, true);
                    String j = new Gson().toJson(clearResult);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    // Write the JSON string to the output stream.
                    writeString(j, respBody);
                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    // We are not sending a response body, so close the response body
                    // output stream, indicating that the response is complete.
                    //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    success = true;
                    // Close the output stream.  This is how Java knows we are done
                    // sending data and the response is complete/
                    exchange.getResponseBody().close();

                }

            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                // client's fault), so we return an "internal server error" status code
                String errorRespMessage = "error";
                RegisterResult registerResult = new RegisterResult(ERROR_MESSAGE, false);
                success1.setSuccess(false);
                String j = new Gson().toJson(registerResult);
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
              //  writeString(j, respBody);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException | DataAccessException | SQLException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            String errorRespMessage = e.toString();
            errorResponse = new ErrorResponse(errorRespMessage, success);
            success1.setSuccess(false);
            String j = new Gson().toJson(errorResponse);
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(j, respBody);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /**
     *    The readString method reads a String from an InputStream.
     * @param is
     * @return
     * @throws IOException
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }


    /**
     * The writeString function writes a string to an outputstream
     * @param str
     * @param os
     * @throws IOException
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
