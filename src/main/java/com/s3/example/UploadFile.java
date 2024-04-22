package com.s3.example;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;

/**
 * @author ：zhumingyuan
 * @description：TODO
 * @date ：2024/4/22 13:09
 */
public class UploadFile {
    static AWSCredentials credentials = new BasicAWSCredentials(
            "", "");
    static AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();

    public static void main(String[] args) {

        PutObjectRequest request = new PutObjectRequest("bucket-mzhu-test1", "test2.txt", new File("/Users/zhumingyuan/java_workspace/MavenTest/src/main/resources/test.txt"));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/text");
        metadata.addUserMetadata("title", "someTitle");
        request.setMetadata(metadata);
        s3Client.putObject(request);
    }
}
