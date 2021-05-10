package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.Gson;

import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;

public class LoginDAO {


    Database db;
    private final String TABLE_NAME = "Login";
    private final String P_KEY_ATTR = "username";
    private final String S_KEY_ATTR = "password";
    private final String USER_P_KEY = "alias";
    private final String USER_S_KEY = "image_url";
    private final String USER_TABLE_NAME = "User";

    public LoginDAO(){
        db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }

    public LoginResponse login(LoginRequest loginRequest) throws Exception {



        Item item = db.getItem(loginRequest.getUsername());
        User user = null;
        String token = null;
        if(item != null){
            String passwd = (String) item.get("password");
            if (checkPassword(loginRequest.getPassword(), passwd)) {
                System.out.println("match");
                Database user_db = new Database(USER_TABLE_NAME, USER_P_KEY, USER_S_KEY);
                Item userItem = user_db.getItem(loginRequest.getUsername());
                String image_url = (String) userItem.get("image_url");
                String alias = (String) userItem.get("alias");
                Map<String, Object> info = (Map<String, Object>) userItem.get("info");
                String firstName = (String) info.get("FirstName");
                String lastName = (String) info.get("LastName");
                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(image_url);
                System.out.println(alias);

                user = new User(firstName, lastName, alias, image_url);
            } else {
                System.out.println("not match");
                user = new User("Test", "User",
                        "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
            }

            String token_table = "AuthToken";
            String p_key = "user_handle";
            String s_key = "authToken";
            db = new Database(token_table, p_key, s_key);
            token = UUID.randomUUID().toString();
            final Map<String, Object> infoMap = new HashMap<String, Object>();
            LocalTime timeNow = LocalTime.now();
            String localTime = new Gson().toJson(timeNow);
            System.out.println(localTime);
            Timeout.setInitialTime(localTime);
            String time = new Gson().toJson(timeNow);
            infoMap.put("date", time);
            db.addItems(infoMap, user.getAlias(), token);
        }


        return new LoginResponse(user, new AuthToken(token));
    }

    public Boolean checkPassword(String candidate, String password) {

        String hash = generateHash(candidate);
        System.out.println(hash);
        System.out.println(password);

        if(hash.equals(password)){
            System.out.println("It is a match");
            return true;
        }
        System.out.println("It does not match");
       return false;
    }

    public  String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length;   idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }


}
