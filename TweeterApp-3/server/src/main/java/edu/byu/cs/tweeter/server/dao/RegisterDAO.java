package edu.byu.cs.tweeter.server.dao;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.StaticClass.Timeout;
import edu.byu.cs.tweeter.server.s3.S3Client;

import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class RegisterDAO {

    S3Client s3Client;
    Database db;
    private final String TABLE_NAME = "User";
    private final String P_KEY_ATTR = "alias";
    private final String S_KEY_ATTR = "image_url";
    public final String DEFAULT_BUCKET_URL = "https://chris-tweeter-images.s3-us-west-2.amazonaws.com/%40";

    public RegisterDAO(){
        this.s3Client = new S3Client();
        this.db = new Database(TABLE_NAME, P_KEY_ATTR, S_KEY_ATTR);
    }
    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public RegisterResponse getRegister(RegisterRequest request) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(request.getImageString());

        String image_url =  s3Client.insert(bytes, request.getUserName());
        User user = null;
        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("FirstName", request.getFirstName());
        infoMap.put("LastName", request.getLastName());

        AddUserCredentials(request.getUserName(), request.getPassword());
        db.addItems(infoMap, request.getUserName(), image_url);
        user = new User(request.getFirstName(), request.getLastName(), request.getUserName(), image_url);

        String token = UUID.randomUUID().toString();
        System.out.println(token);
        Database dao = new Database("AuthToken", "user_handle", "authToken");
        final Map<String, Object> infoMap1 = new HashMap<String, Object>();
        LocalTime timeNow = LocalTime.now();
        String localTime = new Gson().toJson(timeNow);
        Timeout.setInitialTime(localTime);
        String time = new Gson().toJson(timeNow);
        infoMap1.put("date", time);
        dao.addItems(infoMap1, request.getUserName(),token);
        return new RegisterResponse(user, new AuthToken(token));
    }

    public void AddUserCredentials(String username, String password){
       String hashPassword = generateHash(password);
       System.out.println(hashPassword);
       System.out.println(username);
        System.out.println(hashPassword);
        Database loginDb = new Database("Login", "username", "password");
       loginDb.addItems(username, hashPassword);
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
