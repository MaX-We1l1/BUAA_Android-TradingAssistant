package com.example.myapplication.minio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class MinioUtils {
    private MinioConfig configuration;
    private MinioClient minioClient;

    public MinioUtils() {
        configuration = new MinioConfig();
        minioClient = configuration.minioClient();
    }

    /**
     * @param file 上传的文件
     * @param fileName 文件名
     */
    private void upload(File file, String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.length(), -1)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException |
                 IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param file     文件
     * @param fileName 文件名称
     * @Description 上传文件
     */
    public void uploadFile(File file, String fileName) {
        upload(file, fileName);
        // AjaxResult.success("上传成功");
    }

}
