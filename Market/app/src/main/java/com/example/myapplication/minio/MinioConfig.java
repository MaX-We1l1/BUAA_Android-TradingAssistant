package com.example.myapplication.minio;

import io.minio.MinioClient;

public class MinioConfig {
    private String accessKey = "wWJSxzuFAHl236U2gJ6d";
    private String secretKey = "lhoJlxav4vpQAltSTrAG20Uk9mzLJNq6EI5l87hu";
    private String url = "http://127.0.0.1:9090";
    private String bucketName = "android-trade-bucket";

    public MinioConfig() {

    }

    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey,secretKey)
                .build();
    }

    public String getBucketName() {
        return bucketName;
    }
}
