package edu.byu.cs.tweeter.client.model.net;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.byu.cs.tweeter.client.model.domain.Status;
import edu.byu.cs.tweeter.client.model.domain.User;


public class PostGenerator {





    @RequiresApi(api = Build.VERSION_CODES.O)
    String generatePostDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        return formattedDate;
    }
      @RequiresApi(api = Build.VERSION_CODES.O)
      public Status generatePost(User user, String message){
        return new Status(user, "Thu, Oct 15 2020 11:10:20", message);
      }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Status generatePost(User user){
        return new Status(user, "Thu, Oct 15 2020 11:10:20",  "This is a test post");
    }




}
