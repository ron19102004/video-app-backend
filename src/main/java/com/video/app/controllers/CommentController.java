package com.video.app.controllers;

import com.video.app.dto.comment.CreateCommentDto;
import com.video.app.entities.Comment;
import com.video.app.services.CommentService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SecurityUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<DataResponse> addComment(@RequestBody @NotNull CreateCommentDto createCommentDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.commentService.add(authentication.getName(), createCommentDto));
    }

    @GetMapping("")
    public ResponseEntity<DataResponse> getCommentsByVideoId(@RequestParam("videoId") Long videoId) {
        List<Comment> results = this.commentService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok(new DataResponse("Got!", results, true));
    }
}
