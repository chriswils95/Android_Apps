package files.handler;

import files.errorResponse.ErrorResponse;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.result.RegisterResult;
import files.service.RegisterService;
import files.request.RegisterRequest;


import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * The register handler Implements http handler for my /user/register routes it make sure that there is no
 * bad address and
 * parse the necessary exchange body from json to a string to be used in my request and services classes
 */
public class RegisterHandler implements HttpHandler {
     static final String ERROR = "error";
     static final int BUFFER_WRITE_VALUE =  1024;
      final String REQUEST_TYPE = "post";
    /**
     * The handle function implements the http functionlaity of the user register routes like
     * checking for bad address, parsing json data
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

        try {
            // Determine the HTTP request type which is post in this case
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {
                RegisterRequest registerRequest;
                RegisterService registerService = new RegisterService();
                RegisterResult registerResult;
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                        // Get the request body input stream
                        InputStream reqBody = exchange.getRequestBody();
                        // Read JSON string from the input stream
                        String reqData = readString(reqBody);
                        registerRequest = new Gson().fromJson(reqData, RegisterRequest.class);

                        //perform the user registrations by generating his/her family tree and events
                        registerResult = registerService.performRegisterServices(registerRequest);
                        // if the result is null it means the we are trying to register a registered user or there is
                   // connection problems
                        if(registerResult == null){
                            success = false;
                        }
                        else {


                            // Display  the request JSON data
                            System.out.println(reqData);
                            // Start sending the HTTP response to the client, starting with
                            // the status code and any defined headers.
                            // close the output stream, indicating that the response is complete.
                            String j = new Gson().toJson(registerResult);
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = exchange.getResponseBody();
                            // Write the JSON string to the output stream.
                            writeString(j, respBody);
                            // Close the output stream.  This is how Java knows we are done
                            // sending data and the response is complete/
                            exchange.getResponseBody().close();
                          //  respBody.close();

                            success = true;
                        }
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                RegisterResult registerResult1 = new RegisterResult(ERROR,success);
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
                String j = new Gson().toJson(registerResult1);
                writeString(j, respBody);
                // Close the output stream.
                // sending data and the response is complete
                exchange.getResponseBody().close();
            }
        }
        catch (DataAccessException e ){

        } catch (SQLException e) {
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
        char[] buf = new char[BUFFER_WRITE_VALUE];
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