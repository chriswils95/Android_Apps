package edu.byu.cs.tweeter.server.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;

import java.io.InputStream;

import java.io.ByteArrayInputStream;

public class S3Client {
    AmazonS3 s3;
    String bucker_name;
    BasicAWSCredentials awsCreds;

    public String getDEFAULT_BUCKET_URL() {
        return DEFAULT_BUCKET_URL;
    }

    public final String DEFAULT_BUCKET_URL = "https://chris-tweeter-images.s3-us-west-2.amazonaws.com/%40";
    public S3Client() {
        awsCreds = new BasicAWSCredentials("AKIAV5Y67N54EQPANR6B",
                "1YsSVw/b9CCR4jXB4wYauw6v3YASlLb72U5397GP");

        this.s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion("us-west-2")
                .build();
        this.bucker_name = "chris-tweeter-images";
    }

    public String insert(byte [] imageByte, String fileName){
        // Create AmazonS3 object for doing S3 operations
        try {
            fileName = fileName.substring(1);
            InputStream stream = new ByteArrayInputStream(imageByte);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(imageByte.length);
            meta.setContentType("image/JPEG");

            this.s3.putObject(new PutObjectRequest(
                    bucker_name, fileName + ".JPEG", stream, meta)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );




            return s3.getUrl("chris-tweeter-images", fileName + ".JPEG").toString();

        }
        catch(AmazonClientException e){
            e.printStackTrace();
            return  null;
        }
    }

    public boolean insertString(String filename, String stringObjKeyName){
        try {
            // Upload a text string as a new object.
            this.s3.putObject(bucker_name, stringObjKeyName, "Uploaded String Object");
        } catch (AmazonClientException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }

        return true;
    }
}

