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
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：zhumingyuan
 * @description：TODO
 * @date ：2024/4/22 13:09
 */
public class UploadFile {

    private static final Map<String, String> contentTypeMap = new HashMap<>();

    static {
        contentTypeMap.put("txt", "text/plain");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        contentTypeMap.put("mp4", "video/mp4");
    }

    static AWSCredentials credentials = new BasicAWSCredentials(
            "", "");
    static AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();

    public static void main(String[] args) {

        File file = new File("/Users/zhumingyuan/tech_study/amazon-service/S3_java_example/s3_test.xlsx");
        String fileName = file.getName();
        PutObjectRequest request = new PutObjectRequest("bucket-mzhu-test1", "S3_TEST/"+fileName, file);
        // 获取文件扩展名

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 创建 ObjectMetadata 并设置 Content-Type
        ObjectMetadata metadata = new ObjectMetadata();

        // 根据扩展名设置 Content-Type
        String contentType = contentTypeMap.get(extension.toLowerCase());
        if (contentType != null) {
            metadata.setContentType(contentType);
        } else {
            // 如果未知扩展名，设置为二进制流
            metadata.setContentType("application/octet-stream");
        }

        // 创建上传请求并添加元数据
        request.setMetadata(metadata);

        // 上传文件到 S3
        s3Client.putObject(request);
    }
}
