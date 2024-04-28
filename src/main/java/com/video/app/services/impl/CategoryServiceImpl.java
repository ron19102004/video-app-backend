package com.video.app.services.impl;

import com.video.app.aws.AwsS3Service;
import com.video.app.entities.Category;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.CategoryRepository;
import com.video.app.services.CategoryService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidString;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AwsS3Service awsS3Service;
    private ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static final String FOLDER_IMAGE_AWS = "category_image";

    @Override
    public Category create(MultipartFile file, String name) {
        String url = this.awsS3Service.upload(file, "category_image");
        return this.categoryRepository.save(Category.builder()
                .name(name)
                .slug(ValidString.slugify(name))
                .deleted(false)
                .image(url)
                .build());
    }

    @Override
    public List<Category> find() {
        return this.categoryRepository.findByDeleted(false);
    }

    @Override
    public DataResponse updateImage(Long id, MultipartFile file) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Category not found"));
        String imgUrl = category.getImage();
        String url = this.awsS3Service.upload(file, "category_image");
        category.setImage(url);
        executorService.submit(() -> {
            if (!imgUrl.isBlank()) {
                String[] imgUrlSplit = imgUrl.split("/");
                this.awsS3Service.delete(imgUrlSplit[imgUrlSplit.length - 1], FOLDER_IMAGE_AWS);
            }
        });
        return new DataResponse("Updated!", category.getImage(), true);
    }

    @Override
    public DataResponse updateName(Long id, String name) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Category not found"));
        category.setName(name);
        category.setSlug(ValidString.slugify(name));
        this.entityManager.merge(category);
        return new DataResponse("Updated!", null, true);
    }

    @Override
    public DataResponse delete(Long id) {
        Category category = this.categoryRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new ServiceException("Category not found"));
        category.setDeleted(true);
        this.entityManager.merge(category);
        return new DataResponse("Deleted!", null, true);
    }
}
