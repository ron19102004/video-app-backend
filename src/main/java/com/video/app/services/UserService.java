package com.video.app.services;

import com.video.app.entities.User;

public interface UserService {
    User findByUsername(String username);
}
