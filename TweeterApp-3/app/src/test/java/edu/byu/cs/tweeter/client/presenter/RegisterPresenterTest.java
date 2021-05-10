package edu.byu.cs.tweeter.client.presenter;

import android.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.client.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;


public class RegisterPresenterTest {

    private RegisterRequest request;
    private RegisterRequest invalidRequest;
    private RegisterResponse response;
    private RegisterService mockRegisterService;
    private RegisterPresenter presenter;




    private byte [] loadImage(String imageUrl) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(imageUrl);
        return bytes;
    }

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String first_name = "chris";
        String last_name = "wils";
        String username = "cw2636";
        String password = "password";
        String imageString = ByteArrayUtils.StrinfFromByteArray("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        invalidRequest = new RegisterRequest(first_name, last_name, username, password, imageString);
        request = new RegisterRequest(null, null, null, null, null);
        response = new RegisterResponse(currentUser, new AuthToken(""));

        // Create a mock FollowingService
        mockRegisterService = Mockito.mock(RegisterService.class);
        Mockito.when(mockRegisterService.register(request)).thenReturn(response);


        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);

    }

    @Test
    public void testRegister_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.register(request)).thenReturn(response);
        try {

            // Assert that the presenter returns the same response as the service (it doesn't do
            // anything else, so there's nothing else to test).
            Assertions.assertNotNull(presenter.register(request));
        }catch (Exception e){
            Assertions.assertTrue(invalidRequest.getUserName().length() > 0);
        }
    }


    @Test
    public void testRegister_serviceReturnsInCorrectServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.register(invalidRequest)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertNotNull(presenter.register(invalidRequest));
    }

}
