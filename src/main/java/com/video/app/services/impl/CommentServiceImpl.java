package com.video.app.services.impl;

import com.video.app.dto.comment.CreateCommentDto;
import com.video.app.entities.Comment;
import com.video.app.entities.User;
import com.video.app.entities.Video;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.repositories.CommentRepository;
import com.video.app.services.CommentService;
import com.video.app.services.UserService;
import com.video.app.services.VideoService;
import com.video.app.utils.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    @Override
    public DataResponse add(String username, CreateCommentDto createCommentDto) {
        Video video = this.videoService.findById(createCommentDto.videoId());
        User user = this.userService.findByUsername(username);
        Comment parentComment = null;
        if (createCommentDto.parentCommentId() != null && createCommentDto.parentCommentId() != 0) {
            parentComment = this.commentRepository.findById(createCommentDto.parentCommentId())
                    .orElseThrow(() -> new NotFoundEntity("Parent comment not found"));
        }
        Comment comment = Comment.builder()
                .content(createCommentDto.content())
                .user(user)
                .video(video)
                .parentComment(parentComment)
                .build();
        this.commentRepository.save(comment);
        return new DataResponse("Add comment successfully!", null, true);
    }

    @Override
    public List<Comment> getCommentsByVideoId(Long videoId) {
        return this.commentRepository.findAllByVideoId(videoId);
    }
}
