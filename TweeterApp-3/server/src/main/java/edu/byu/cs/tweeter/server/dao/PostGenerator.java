package edu.byu.cs.tweeter.server.dao;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;


public class PostGenerator {





    String generatePostDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        return formattedDate;
    }
      public Status generatePost(User user, String message){
        return new Status(user, "Thu, Oct 15 2020 11:10:20", message);
      }

    public Status generatePost(User user){
        return new Status(user, "Thu, Oct 15 2020 11:10:20",  "This is a test post");
    }




}
