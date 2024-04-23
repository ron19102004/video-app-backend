package com.video.app.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {
    @Value("${cloud.aws.bucket.name}")
    private String bucketName;
    @Value("${cloud.endpointUrl}")
    private String endpointUrl;
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private ServletContext servletContext;

    @Override
    public String upload(MultipartFile multipartFile, String folderName) {
        final File file = this.convertMultipartFileToFile(multipartFile);
        final String fileName = folderName + "/" + System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        final String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
        final PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, fileName, file);
        putRequest.withCannedAcl(CannedAccessControlList.PublicReadWrite);
        this.s3Client.putObject(putRequest);
        this.scheduleFileDeletion(file);
        return fileUrl;
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File fileConvert = null;
        try {
            String rootPath = servletContext.getRealPath("/");
            File tempDir = new File(rootPath + File.separator + "temp");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            fileConvert = File.createTempFile("temp", null, tempDir);
            multipartFile.transferTo(fileConvert);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileConvert;
    }

    private void scheduleFileDeletion(File file) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }, 30, TimeUnit.MILLISECONDS);
        executorService.shutdown();
    }

    @Override
    public byte[] download(String fileName, String folder) {
        final String _fileName = folder + "/" + fileName;
        final S3Object object = this.s3Client.getObject(this.bucketName, _fileName);
        S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
        try {
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String fileName, String folder) {
        try {
            final String _fileName = folder + "/" + fileName;
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucketName, _fileName);
            this.s3Client.deleteObject(deleteObjectRequest);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
        }
    }
}
