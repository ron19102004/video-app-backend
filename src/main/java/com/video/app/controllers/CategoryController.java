package com.video.app.controllers;

import com.video.app.entities.Category;
import com.video.app.exceptions.GlobalException;
import com.video.app.services.CategoryService;
import com.video.app.utils.DataResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends GlobalException {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<DataResponse> create(
            @RequestParam("name") @NotNull String name,
            @NotNull @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(DataResponse.builder()
                .status(true)
                .message("Created!")
                .data(this.categoryService.create(file, name))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(this.categoryService.find());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/update-image")
    public ResponseEntity<DataResponse> updateImage(
            @NotNull @PathVariable("id") Long id,
            @NotNull @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.categoryService.updateImage(id, file));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/update-name")
    public ResponseEntity<DataResponse> updateName(
            @NotNull @PathVariable("id") Long id,
            @NotNull @RequestParam("name") String name) {
        return ResponseEntity.ok(this.categoryService.updateName(id, name));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> delete(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.categoryService.delete(id));
    }

}
