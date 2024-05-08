package com.video.app.services;

import com.video.app.entities.Category;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.utils.DataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    Category create(MultipartFile file, String name);

    List<Category> find();

    DataResponse updateImage(Long id, MultipartFile file) throws NotFoundEntity;

    DataResponse updateName(Long id, String name) throws NotFoundEntity;

    DataResponse delete(Long id) throws NotFoundEntity;
    Category findById(Long id);
}
