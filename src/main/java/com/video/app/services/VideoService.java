package com.video.app.services;

import com.video.app.dto.video.ChangePrivacyDto;
import com.video.app.dto.video.CreateInfoVideoDto;
import com.video.app.entities.Video;
import com.video.app.utils.DataResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    DataResponse createInfoVideo(String username, CreateInfoVideoDto createInfoVideoDto);

    DataResponse uploadImage(String username, Long id, MultipartFile file);

    DataResponse uploadVideo(String username, Long id, MultipartFile file);

    Video findById(Long id);

    List<Video> search(Long categoryId, Long countryId, String name);

    Page<Video> findAllWithPage(int pageNumber);

    Page<Video> findAllWithPageAndUploaderId(int pageNumber, Long uploaderId);

    Video findBySlug(String slug);

    List<Video> findAllByUsername(String username);

    DataResponse delete(String username,Long id) ;

    DataResponse changePrivacyVip(String username, Long id, ChangePrivacyDto changePrivacyDto);
}
