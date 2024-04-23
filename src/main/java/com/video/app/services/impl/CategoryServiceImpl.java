package com.video.app.services.impl;

import com.video.app.aws.AwsS3Service;
import com.video.app.entities.Category;
import com.video.app.repositories.CategoryRepository;
import com.video.app.services.CategoryService;
import com.video.app.utils.ValidString;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AwsS3Service awsS3Service;

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
}
