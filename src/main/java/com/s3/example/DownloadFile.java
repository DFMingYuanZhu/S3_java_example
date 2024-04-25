package com.s3.example;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * @author ：zhumingyuan
 * @description：TODO
 * @date ：2024/4/22 13:10
 */
public class DownloadFile {

    public static void main(String[] args) throws IOException {

        Regions clientRegion = Regions.US_EAST_1;
        String bucketName = "bucket-mzhu-test1";
        String objectKey = "s3_test";
        long expirationTimeMillis = 3600000; // 1 hour
        AWSCredentials credentials = new BasicAWSCredentials(
                "", "");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(clientRegion)
                .build();

        // Generate the pre-signed URL
        Date expiration = new Date(System.currentTimeMillis() + expirationTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        //.withContentType("text/plain")
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        //generatePresignedUrlRequest.putCustomRequestHeader("x-amz-meta-referer", "http://localhost:63342");
        //generatePresignedUrlRequest.addRequestParameter("response-content-disposition", "attachment; filename=\"downloaded-file.txt\"");
        URL preSignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        // Print the pre-signed URL
        System.out.println("Pre-signed URL: " + preSignedUrl);
    }
}
