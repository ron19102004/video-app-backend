package com.video.app.services;

import com.video.app.entities.Category;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
    Category create(MultipartFile file, String name);
}
