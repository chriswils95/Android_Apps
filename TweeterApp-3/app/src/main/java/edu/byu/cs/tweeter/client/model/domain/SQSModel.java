package edu.byu.cs.tweeter.client.model.domain;

import java.io.Serializable;
import java.util.Set;

public class SQSModel  implements Serializable {

    Status status;
    Set<String> userAliases;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<String> getUserAliases() {
        return userAliases;
    }

    public void setUserAliases(Set<String> userAliases) {
        this.userAliases = userAliases;
    }

    public SQSModel(Status status, Set<String> userAliases){
        this.status = status;
        this.userAliases = userAliases;
    }
}

