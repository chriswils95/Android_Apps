package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;


public class RegisterServiceImplTest {

    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterServiceImpl registerServiceSpy;



    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws Exception {


        String first_name = "chris";
        String last_name = "wils";
        String username = "cw2636";
        String password = "password";
        String imageString = StrinfFromByteArray("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");


        // Setup request objects to use in the tests
        validRequest = new RegisterRequest(first_name, last_name, username, password, imageString);
        invalidRequest = new RegisterRequest(null,  null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(currentUser, new AuthToken("gcgjcccgh"));
        RegisterDAO registerDAO = Mockito.mock(RegisterDAO.class);
        Mockito.when(registerDAO.getRegister(validRequest)).thenReturn(successResponse);

        failureResponse = new RegisterResponse("An exception occured");
        Mockito.when(registerDAO.getRegister(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        registerServiceSpy = Mockito.spy(new RegisterServiceImpl());
        Mockito.when(registerServiceSpy.getRegisterDAO()).thenReturn(registerDAO);
    }

    /**
     * Verify that for successful requests the {@link RegisterService#register(RegisterRequest)}
     * method returns the same result as the {@link RegisterDAO}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testLogin_validRequest_correctResponse() throws Exception {
        RegisterResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    /**
     * Verify that the {@link RegisterService#register(RegisterRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testRegister_validRequest_loadsProfileImages() throws Exception {
        RegisterResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertNotNull(response.getUser().getImageUrl());
    }

    /**
     * Verify that for failed requests the {@link RegisterService#register(RegisterRequest)}
     * method returns the same result as the {@link RegisterDAO}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testRegister_invalidRequest_returnsNoUser() throws Exception {
        RegisterResponse response = registerServiceSpy.register(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }

    public static byte [] bytesFromUrl(String urlString) throws IOException {
        InputStream URLcontent = null;
        HttpURLConnection connection = null;

        try {

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return bytesFromInputStream(inputStream);
            } else {
                throw new IOException("Unable to read from url. Response code: " + connection.getResponseCode());
            }
//            }
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }



    public static String StrinfFromByteArray(String url) throws IOException {
        byte [] bytes = bytesFromUrl(url);
        String imageString = Base64.getEncoder().encodeToString(bytes);
        return imageString;
    }

    /**
     * Reads the bytes from the specified input stream.
     *
     * @param inputStream the stream where the bytes to be read reside.
     * @return the bytes.
     * @throws IOException if an I/O error occurs while attempting to read from the stream.
     */
    public static byte [] bytesFromInputStream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int numbRead;
        byte[] data = new byte[1024];
        while ((numbRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, numbRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
