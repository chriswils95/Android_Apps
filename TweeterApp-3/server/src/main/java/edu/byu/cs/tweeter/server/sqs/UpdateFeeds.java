package edu.byu.cs.tweeter.server.sqs;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateFeeds {

    String queueUrl = "";

    private BasicAWSCredentials awsCreds;
    String tableName;
    String primaryKeyAttr;
    String sortKeyAttr;


    AmazonDynamoDB client;
    private DynamoDB dynamoDB;
    AmazonSQS sqs;

    public UpdateFeeds(){
        awsCreds =  new BasicAWSCredentials("AKIAV5Y67N54EQPANR6B",
                "1YsSVw/b9CCR4jXB4wYauw6v3YASlLb72U5397GP");

        sqs = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();        queueUrl = "https://sqs.us-west-2.amazonaws.com/407549996920/task4";
    }



            public void sendMessage(String messageBody){



                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(messageBody)
                        .withDelaySeconds(15);


//                Map<String, String> info = new HashMap<>()
//;
//                info.put("message", messageBody);
//
//                SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
//                        .withQueueUrl(queueUrl)
//                        .withEntries(
//                                new SendMessageBatchRequestEntry(
//                                        "msg_1", "Hello from message 1"),
//                                new SendMessageBatchRequestEntry(
//                                        "msg_2", "Hello from message 2")
//                                        .withDelaySeconds(10));
//                sqs.sendMessageBatch(send_batch_request);
                SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

                String msgId = send_msg_result.getMessageId();
            }

}
