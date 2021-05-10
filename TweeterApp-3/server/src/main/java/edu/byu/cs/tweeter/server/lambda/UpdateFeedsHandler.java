package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.SQSModel;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.Dynamodb.Database;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {



    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            Status status = null;
            String message = msg.getBody().toString();

            SQSModel sqsModel = null;
            Set<String> alias = new HashSet<String>();


            if (message != null || !message.isEmpty()) {

                 Database db = new Database("Feed", "user_alias","date");
                 String date = getPostDate();
                  sqsModel = new Gson().fromJson(message, SQSModel.class);
                  status = sqsModel.getStatus();
                  alias = sqsModel.getUserAliases();
                List<String> aList = new ArrayList<String>(alias);
                Map<String, Object> infoMap = new HashMap<String, Object>();
                String entityBody = new Gson().toJson(status);
                infoMap.put("Status", entityBody);
                db.writeToBatch(aList, date, infoMap);


            }
        }
            return null;
    }

    String getPostDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        return formattedDate;
    }
}
