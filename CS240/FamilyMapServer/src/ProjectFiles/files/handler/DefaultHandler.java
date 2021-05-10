/**
 * The deafult handler Implements http handler for my / and /main.css routes it make sure that there is no bad
 * address throw a 404 html error message if the routes doesnt matches the above
 */


package files.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;





public class DefaultHandler implements HttpHandler {

    static final String REQUEST_TYPE = "get";
    static final String HOME_PAGE_PATH = "/" ;
    static final String CSS_PATH = "/css/main.css";

    public DefaultHandler(){
    }

    /**
     * The handle function implements the Http handles and take care of all the necessary functionality
     * of the default handler class like sending html files, csss files and an error message if the routes
     * doesnt matches any user routes
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

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            // This operation requires a POST request, because the
            // client is "posting" information to the server for processing.
            if (exchange.getRequestMethod().toLowerCase().equals(REQUEST_TYPE)) {
                URI uri = exchange.getRequestURI();

                String uriPath = uri.getPath();

                if (uriPath.equals(HOME_PAGE_PATH)) {
                    String filePathStr = "web/index.html";
                    Path filePath = FileSystems.getDefault().getPath(filePathStr);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(filePath, exchange.getResponseBody());
                    // Get the HTTP request headers
                   // Headers reqHeaders = exchange.getRequestHeaders();


                    // Extract the JSON string from the HTTP request body

                    // Get the request body input stream
                  //  InputStream reqBody = exchange.getRequestBody();
                    // Read JSON string from the input stream

                    // Display/log the request JSON data

                    // TODO: Claim a route based on the request data
                    // We are not sending a response body, so close the response body
                    // output stream, indicating that the response is complete.
                      exchange.getResponseBody().close();
                    //reqBody.close();

                    success = true;
                }
                else if(uriPath.equals(CSS_PATH)){
                    String filePathStr = "web/css/main.css";
                    Path filePath = FileSystems.getDefault().getPath(filePathStr);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(filePath, exchange.getResponseBody());
                    // Get the HTTP request headers
                    Headers reqHeaders = exchange.getRequestHeaders();
                    exchange.getResponseBody().close();

                    success = true;
                }
                else{


                    throw new IOException();
                }
            }
            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                String filePathStr = "web/HTML/404.html";
                Path filePath = FileSystems.getDefault().getPath(filePathStr);
                //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                Files.copy(filePath, exchange.getResponseBody());
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            // The HTTP request was invalid somehow, so we return a "bad request"
            // status code to the client.
            String filePathStr = "web/HTML/404.html";
            Path filePath = FileSystems.getDefault().getPath(filePathStr);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            Files.copy(filePath, exchange.getResponseBody());
            // Get the HTTP request headers
            Headers reqHeaders = exchange.getRequestHeaders();
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
}
