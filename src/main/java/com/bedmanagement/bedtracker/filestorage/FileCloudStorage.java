package com.bedmanagement.bedtracker.filestorage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class FileCloudStorage {

    @Autowired
    private AmazonS3 s3Client;

    public void uploadFile(byte[] file,String email) {
        InputStream targetStream = new ByteArrayInputStream(file);
        s3Client.putObject(new PutObjectRequest("hospitalbedtracker", email, targetStream,new ObjectMetadata()));
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject("hospitalbedtracker", fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject("hospitalbedtracker", fileName);
        return fileName + " removed ...";
    }




}
