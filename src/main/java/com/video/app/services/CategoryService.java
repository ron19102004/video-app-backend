package com.video.app.services;

import com.video.app.entities.Category;
import com.video.app.exceptions.ServiceException;
import com.video.app.utils.DataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    Category create(MultipartFile file, String name);

    List<Category> find();

    DataResponse updateImage(Long id, MultipartFile file) throws ServiceException;

    DataResponse updateName(Long id, String name) throws ServiceException;

    DataResponse delete(Long id) throws ServiceException;
}
