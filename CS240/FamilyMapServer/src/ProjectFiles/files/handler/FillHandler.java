
/**
 * The fill handler Implements http handler for my /fillroutes it make sure that there is no bad
 * address and
 * parse the necessary exchange body from json to a string to be used in my request and services classes
 */


package files.handler;

import files.errorResponse.ErrorResponse;
import files.result.FillResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import files.dao.DataAccessException;
import files.service.FillService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.sql.SQLException;
import java.util.Scanner;

public class FillHandler implements HttpHandler {
    static final String REQUEST_TYPE = "post";
    static final String SPLIT_TOKEN = "/";
    static final String BAD_ADDRESS_MESSAGE = "bad request";


    /**
     * The handle function implements the http functionlaity of the fill routes like
     * checking for bad address, parsing to be used in my request class and writing to an output stream
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
        FillResult fillResult = null;
        ErrorResponse errorResponse = null;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present

                URI uri = exchange.getRequestURI();
                String respData = null;
                String uriPath = uri.getPath();
                Scanner scanner = new Scanner(uriPath).useDelimiter(SPLIT_TOKEN);
                String defaultHeader = scanner.next();
                String username = scanner.next();

                FillService fillService = new FillService();
                int generations = 0;
                while (scanner.hasNext()) {
                    generations = scanner.nextInt();
                }

                if (generations == 0) {
                    respData = fillService.performedFilledService(username, 4);
                } else {
                    respData = fillService.performedFilledService(username, generations);
                }


                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                fillResult = new FillResult(respData, true);
                String j = new Gson().toJson(fillResult);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
                //respBody.write(j.getBytes());
                writeString(j, respBody);
                // Write the JSON string to the output stream.
                //  respBody.write(j.getBytes());
                // Close the output stream.  This is how Java knows we are done
                // sending data and the response is complete/
                exchange.getResponseBody().close();
                //respBody.close();

                success = true;
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                OutputStream respBody = exchange.getResponseBody();
                errorResponse = new ErrorResponse(BAD_ADDRESS_MESSAGE, success);
                String error = new Gson().toJson(errorResponse);
                writeString(error, respBody);

                exchange.getResponseBody().close();
                //respBody.close();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
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
