package com.video.app.services.impl;

import com.video.app.aws.AwsS3Service;
import com.video.app.dto.video.CreateInfoVideoDto;
import com.video.app.entities.*;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.VideoRepository;
import com.video.app.services.CategoryService;
import com.video.app.services.CountryService;
import com.video.app.services.UserService;
import com.video.app.services.VideoService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private UserService userService;
    @Autowired
    private AwsS3Service awsS3Service;
    private static final String FOLDER_VIDEO_IMAGE_AWS = "img_videos";
    private static final String FOLDER_VIDEO_AWS = "videos";
    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    @Override
    public DataResponse createInfoVideo(String username, CreateInfoVideoDto createInfoVideoDto) {
        User user = this.userService.findByUsername(username);
        if (!user.getConfirmed() && user.getRole() != Role.ADMIN)
            throw new ServiceException("You have not permission!!", HttpStatus.FORBIDDEN);
        Category category = this.categoryService.findById(createInfoVideoDto.categoryId());
        Country country = this.countryService.findById(createInfoVideoDto.countryId());
        Video video = Video.builder()
                .category(category)
                .country(country)
                .uploader(user)
                .name(createInfoVideoDto.name())
                .slug(SlugUtils.slugify(createInfoVideoDto.name() + "-" + System.currentTimeMillis()))
                .description(createInfoVideoDto.description())
                .deleted(false)
                .privacy(createInfoVideoDto.isPublic() ? Privacy.PUBLIC : Privacy.PRIVATE)
                .vip(createInfoVideoDto.isVip())
                .release(createInfoVideoDto.release())
                .duration("")
                .tag(createInfoVideoDto.tag())
                .build();
        Video videoUploaded = this.videoRepository.save(video);
        return new DataResponse("Video uploaded!", videoUploaded, true);
    }

    @Override
    public DataResponse uploadImage(String username, Long id, MultipartFile file) {
        User user = this.userService.findByUsername(username);
        Video video = this.findById(id);
        if (!user.getConfirmed() && user.getRole() != Role.ADMIN)
            throw new ServiceException("You have not permission!!", HttpStatus.FORBIDDEN);
        String img = video.getImage();
        String url = this.awsS3Service.upload(file, FOLDER_VIDEO_IMAGE_AWS);
        video.setImage(url);
        this.entityManager.merge(video);
        if (img != null && !img.isBlank()) {
            this.executorService.submit(() -> {
                String[] name = img.split("/");
                this.awsS3Service.delete(name[name.length - 1], FOLDER_VIDEO_IMAGE_AWS);
            });
        }
        return new DataResponse("Uploaded!", null, true);
    }

    @Override
    public DataResponse uploadVideo(String username, Long id, MultipartFile file) {
        User user = this.userService.findByUsername(username);
        Video video = this.findById(id);
        if (!user.getConfirmed() && user.getRole() != Role.ADMIN)
            throw new ServiceException("You have not permission!!", HttpStatus.FORBIDDEN);
        String src = video.getSrc();
        String url = this.awsS3Service.upload(file, FOLDER_VIDEO_AWS);
        video.setSrc(url);
        this.entityManager.merge(video);
        if (src != null && !src.isBlank()) {
            this.executorService.submit(() -> {
                String[] name = src.split("/");
                this.awsS3Service.delete(name[name.length - 1], FOLDER_VIDEO_AWS);
            });
        }
        return new DataResponse("Uploaded!", null, true);
    }

    @Override
    public Video findById(Long id) {
        return this.videoRepository.findById(id).orElseThrow(() -> new NotFoundEntity("Video not found"));
    }

    @Override
    public List<Video> search(Long categoryId, Long countryId, String name) {
        if (categoryId != null && countryId != null && name != null) {
            this.categoryService.findById(categoryId);
            this.countryService.findById(countryId);
            return this.videoRepository.findAllByCountryIdAndCategoryIdAndNameLike(countryId, categoryId, name);
        } else if (categoryId != null && countryId != null && name == null) {
            this.categoryService.findById(categoryId);
            this.countryService.findById(countryId);
            return this.videoRepository.findAllByCountryIdAndCategoryId(countryId, categoryId);
        } else if (categoryId != null && countryId == null && name != null) {
            this.categoryService.findById(categoryId);
            return this.videoRepository.findAllByCategoryIdAndNameLike(categoryId, name);
        } else if (categoryId != null && countryId == null && name == null) {
            this.categoryService.findById(categoryId);
            return this.videoRepository.findAllByCategoryId(categoryId);
        } else if (categoryId == null && countryId != null && name == null) {
            this.countryService.findById(countryId);
            return this.videoRepository.findAllByCountryId(countryId);
        } else if (categoryId == null && countryId != null && name != null) {
            this.countryService.findById(countryId);
            return this.videoRepository.findAllByCountryIdAndNameLike(countryId, name);
        } else if (categoryId == null && countryId == null && name != null) {
            return this.videoRepository.findAllByNameLike(name);
        }
        return List.of();
    }

    @Override
    public Page<Video> findAllWithPage(int pageNumber) {
        Pageable pageable = PageRequest.of((pageNumber * 0), 9, Sort.by("id").descending());
        return this.videoRepository.findAll(pageable);
    }

    @Override
    public Page<Video> findAllWithPageAndUploaderId(int pageNumber, Long uploaderId) {
        Pageable pageable = PageRequest.of((pageNumber * 0), 9,Sort.by("id").descending());
        return this.videoRepository.findAllByUploaderId(uploaderId, pageable);
    }
}
