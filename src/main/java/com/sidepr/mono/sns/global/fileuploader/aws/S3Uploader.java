package com.sidepr.mono.sns.global.fileuploader.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.sidepr.mono.sns.global.fileuploader.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
public class S3Uploader implements FileUploader {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file, String dirName) throws IOException {
        if(file.isEmpty()){
            return null;
        }
        
        String originalFilename = file.getOriginalFilename();
        String fileName = dirName + "/" + FileUploader.createStoreFileName(originalFilename);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objectMetadata.setContentLength(bytes.length);

        s3Client.putObject(
                new PutObjectRequest(bucket, fileName, file.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        
        return s3Client
                .getUrl(bucket, fileName)
                .toString();
    }

    @Override
    public void delete(String filename) {
        s3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
    }


}