package com.s3.example;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

/**
 * @author ：zhumingyuan
 * @description：TODO
 * @date ：2024/4/22 13:09
 */
public class CreateBucket {

    public static void main(String[] args) {

        Regions clientRegion = Regions.US_EAST_1;
        String bucketName = "bucket-mzhu-test1";
        try {

        AWSCredentials credentials = new BasicAWSCredentials(
                "", "");

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(clientRegion)
                .build();

        if (!s3Client.doesBucketExistV2(bucketName)) {
            // Because the CreateBucketRequest object doesn't specify a region, the
            // bucket is created in the region specified in the client.
            s3Client.createBucket(new CreateBucketRequest(bucketName));

            // Verify that the bucket was created by retrieving it and checking its
            // location.
            String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
            System.out.println("Bucket location: " + bucketLocation);
        } else {
            s3Client.listBuckets().stream().forEach(b -> System.out.println(b.getName()));
        }
    } catch (AmazonServiceException e) {
        // The call was transmitted successfully, but Amazon S3 couldn't process
        // it and returned an error response.
        e.printStackTrace();
    } catch (SdkClientException e) {
        // Amazon S3 couldn't be contacted for a response, or the client
        // couldn't parse the response from Amazon S3.
        e.printStackTrace();
    }
    }
}
