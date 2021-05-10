package edu.byu.cs.tweeter.server.lambda;


import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.SQSModel;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.Dynamodb.Database;
import edu.byu.cs.tweeter.server.Dynamodb.ResultsPage;
import edu.byu.cs.tweeter.server.sqs.FollowersAlias;

public class GetFollowersAliasHandler implements RequestHandler<SQSEvent, Void> {

        @Override
        public Void handleRequest(SQSEvent event, Context context) {
            for (SQSEvent.SQSMessage msg : event.getRecords()) {


                Status status = null;
                String message = msg.getBody().toString();
                if(message != null){
                 status = new Gson().fromJson(message, Status.class);
                }

                System.out.println(msg.getBody().toString());

                SQSModel sqsModel = null;

                Set<String> alias = getAliases(status.getUser().getAlias());

                System.out.println(alias.size());

                if(alias.size() > 0) {

                    if (alias.size() > 25) {
                        Iterator<String> it = alias.iterator();
                        int index = 0;
                        Set<String> temp = new HashSet<String>();
                        while (it.hasNext()) {
                            String temp_alias = it.next();
                            if (index == 25) {
                                sqsModel = new SQSModel(status, temp);
                                String entityBody = new Gson().toJson(sqsModel);
                                FollowersAlias followersAlias = new FollowersAlias();
                                followersAlias.sendMessage(entityBody);

                                index = 0;
                                temp = new HashSet<String>();
                            } else {
                                index++;
                            }
                            temp.add(temp_alias);
                        }

                    } else {
                        sqsModel = new SQSModel(status, alias);
                        String entityBody = new Gson().toJson(sqsModel);
                        FollowersAlias followersAlias = new FollowersAlias();
                        followersAlias.sendMessage(entityBody);
                    }
                }





                // TODO:
                // Add code to print message body to the log
            }
            return null;
        }


        public Set<String> getAliases(String alias){

            Database db = new Database("Follower",
                    "followee_handle", "follower_handle");
            ResultsPage results = null;

            Set<String> followeeAlias = new HashSet<>();

            System.out.println(alias);
            String lastFollowerAlias = null;

            while (results == null || results.hasLastKey()) {
                lastFollowerAlias = ((results != null) ? results.getLastKey() : null);
                results = db.getFollowers(alias, 10, lastFollowerAlias);
                followeeAlias.addAll(results.getValues());
            }

            return followeeAlias;
        }




}
