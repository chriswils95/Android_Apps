package edu.byu.cs.tweeter.client.model.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {

    String token;

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
