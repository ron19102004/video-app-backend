package com.video.app.controllers;

import com.video.app.dto.video.CreateInfoVideoDto;
import com.video.app.entities.Video;
import com.video.app.services.VideoService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SecurityUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/new-info")
    public ResponseEntity<DataResponse> createInfo(@RequestBody CreateInfoVideoDto createInfoVideoDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.videoService.createInfoVideo(authentication.getName(), createInfoVideoDto));
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/upload-image/{id}")
    public ResponseEntity<DataResponse> uploadImg(@NotNull @PathVariable("id") Long id, @NotNull @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.videoService.uploadImage(authentication.getName(), id, file));
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/upload-video/{id}")
    public ResponseEntity<DataResponse> uploadVideo(@NotNull @PathVariable("id") Long id, @NotNull @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.videoService.uploadVideo(authentication.getName(), id, file));
    }

    @GetMapping("/search")
    public ResponseEntity<DataResponse> searchByCateCountryAndName(@RequestParam(value = "category_id", required = false) Long categoryId,
                                                            @RequestParam(value = "country_id", required = false) Long countryId,
                                                            @RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(new DataResponse("Found", this.videoService.search(categoryId, countryId, name), true));
    }

    @GetMapping("")
    public ResponseEntity<DataResponse> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                                                @RequestParam(value = "uploader_id", required = false) Long uploaderId) {
        Page<Video> videos;
        if (uploaderId != null) {
            videos = this.videoService.findAllWithPageAndUploaderId(pageNumber, uploaderId);
        } else {
            videos = this.videoService.findAllWithPage(pageNumber);
        }
        return ResponseEntity.ok(new DataResponse("Found", videos.getContent(), true));
    }

    @GetMapping("/item/{slug}")
    public ResponseEntity<DataResponse> findBySlug(@PathVariable("slug") String slug) {
        Video video = this.videoService.findBySlug(slug);
        DataResponse dataResponse;
        if (video == null) dataResponse = new DataResponse("Slug not found video !", null, false);
        else dataResponse = new DataResponse("Found!", video, true);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/my-videos")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> findAllByUsername() {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse("Found!",
                        this.videoService.findAllByUsername(authentication.getName()),
                        true
                )
        );
    }
}
