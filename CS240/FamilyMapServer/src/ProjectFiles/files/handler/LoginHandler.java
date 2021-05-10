/**
 * The login handler class implements the http handler tasks for  the login routes
 */

package files.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.errorResponse.ErrorResponse;
import files.request.LoginRequest;
import files.result.EventsResult;
import files.result.LoginResult;
import files.result.PersonsResult;
import files.service.LoginService;
import files.success.Success;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * The Login handler Implements http handler for my user/login routes it make sure that there
 * is no bad address and
 * parse the necessary exchange body from json to a string to be used in my request and services classes
 */
public class LoginHandler implements HttpHandler {

    static final String REQUEST_TYPE = "post";
    static final String ERROR_MESSAGE = "error";


    public LoginHandler(){
            }

    /**
     * The handle function implements the http functionlaity of the user/login routes like
     * checking for bad address and parsing json
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // However, it does demonstrate the following:
        // 1. How to get the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to read JSON data from the HTTP request body
        // 4. How to return the desired status code (200, 404, etc.)
        //		in an HTTP response
        // 5. How to check an incoming HTTP request for an auth token

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
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {
                    LoginRequest loginRequest;
                    LoginService loginService = new LoginService();
                    isLogin = true;
                   String respData = null;
                // Get the HTTP request headers
                    Headers reqHeaders = exchange.getRequestHeaders();
                    // Check to see if an "Authorization" header is present

                    // Get the request body input stream
                    InputStream reqBody = exchange.getRequestBody();
                    // Read JSON string from the input stream
                    String reqData = readString(reqBody);

                    loginRequest = new Gson().fromJson(reqData, LoginRequest.class);
                    loginResult = loginService.performLoginServices(loginRequest);

                    // Display/log the request JSON data
                    System.out.println(reqData);

                    //reqBody.close();
                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    // We are not sending a response body, so close the response body
                    // output stream, indicating that the response is complete.
                    //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);


                   if(loginResult != null) {

                       String j = new Gson().toJson(loginResult);
                       exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                       OutputStream respBody = exchange.getResponseBody();
                       // Write the JSON string to the output stream.
                       writeString(j, respBody);
                       // Close the output stream.  This is how Java knows we are done
                       // sending data and the response is complete/
                       exchange.getResponseBody().close();
                       //respBody.close();
                       success = true;
                        }
                    }

            if (!success) {
               loginResult = new LoginResult(ERROR_MESSAGE, success);
               String error1 = new Gson().toJson(loginResult);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                   writeString(error1, respBody);

                // We are not sending a response body, so close the response body
                exchange.getResponseBody().close();
                //respBody.close();
            }
        }
        catch (DataAccessException | SQLException e ) {
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
           // respBody.close();
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
