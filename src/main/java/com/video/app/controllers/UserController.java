package com.video.app.controllers;

import com.video.app.entities.User;
import com.video.app.services.UserService;
import com.video.app.utils.DataResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<DataResponse> info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated or missing token", null, false));
        }
        Object data = authentication.getPrincipal();
        return ResponseEntity.ok(new DataResponse("Info", data, true));
    }

    @PatchMapping("/image")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> updateImage(@RequestParam("file") @NotNull MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated or missing token", null, false));
        }
        String username = authentication.getName();
        return ResponseEntity.ok(this.userService.updateImage(username, file));
    }
}
