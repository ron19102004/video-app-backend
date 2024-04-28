package com.video.app.services.impl;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.video.app.aws.AwsS3Service;
import com.video.app.entities.User;
import com.video.app.repositories.UserRepository;
import com.video.app.services.UserService;
import com.video.app.utils.DataResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AwsS3Service awsS3Service;
    private static final String FOLDER_IMAGE_AWS = "avatars";
    @PersistenceContext
    private EntityManager entityManager;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);

    }

    @Override
    public DataResponse updateImage(String username, MultipartFile file) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        String imgUrl = user.getImageURL();
        String url = this.awsS3Service.upload(file, FOLDER_IMAGE_AWS);
        user.setImageURL(url);
        this.entityManager.merge(user);
        executorService.submit(() -> {
            if (!imgUrl.isBlank()) {
                String[] imgUrlSplit = imgUrl.split("/");
                this.awsS3Service.delete(imgUrlSplit[imgUrlSplit.length - 1], FOLDER_IMAGE_AWS);
            }
        });
        return new DataResponse("Image updated!", null, true);
    }
}
