package com.s3.example;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：zhumingyuan
 * @description：TODO
 * @date ：2024/4/22 13:10
 */
public class MultiPartUploadFile {

    private List<URL> generatePresignUploadUrl() {
        Regions clientRegion = Regions.US_EAST_1;
        String bucketName = "bucket-mzhu-test1";
        String objectKey = "presign/test3.txt";
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("",
                "");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectKey);
        InitiateMultipartUploadResult initResult = s3Client.initiateMultipartUpload(initRequest);
        String uploadId = initResult.getUploadId(); // 上传 ID

        int partSize = 10 * 1000 * 1000; //10MB each part
        int totalParts = 3; // upload 3 parts
        List<PartETag> partETags = new ArrayList<>();

        List<URL> result = new ArrayList<>();
        // generate presign for each part
        for (int i = 1; i <= totalParts; i++) {
            GeneratePresignedUrlRequest presignedRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
            presignedRequest.setMethod(HttpMethod.PUT);
            presignedRequest.setExpiration(new Date(System.currentTimeMillis() + 3600000));
            presignedRequest.addRequestParameter("partNumber", String.valueOf(i));
            presignedRequest.addRequestParameter("uploadId", uploadId);

            URL presignedUrl = s3Client.generatePresignedUrl(presignedRequest);
            result.add(presignedUrl);
            System.out.println("uploadId: " + uploadId + " Presigned URL for part " + i + ": " + presignedUrl);
        }
        return result;
    }

    private void CompleteMultiPartUpload() {
        Regions clientRegion = Regions.US_EAST_1;
        String bucketName = "bucket-mzhu-test1";
        String objectKey = "presign/test3.txt";
        String uploadId = "qy7vcfOKlX52bf1Y67nY4dcHPk0KUc6A25chgdXSXvEzl5rgaxtumhzfCzWVoztdruTaQJcD4N_ONom2WUSCEg--";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                "", "");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(clientRegion) // 替换为你的区域
                .build();


        List<PartETag> partETags = new ArrayList<>();
        partETags.add(new PartETag(1, "a7b7823d1906fd43280247ed75ec4f99")); // 替换 PartNumber 和 ETag
        partETags.add(new PartETag(2, "51bc8f9f12b5a7077b0ceb6d7b93821d"));
        partETags.add(new PartETag(3, "35f9ab6ddc878790ec263b5b146af06c"));

        CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                bucketName,
                objectKey,
                uploadId,
                partETags
        );

        CompleteMultipartUploadResult completeResult = s3Client.completeMultipartUpload(completeRequest);

        System.out.println("Multipart upload completed: " + completeResult.getLocation());
    }
}
