package com.video.app.services;

import com.video.app.entities.User;
import com.video.app.utils.DataResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findByUsername(String username);

    DataResponse updateImage(String username, MultipartFile file);
}
