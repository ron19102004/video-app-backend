package com.video.app.services;

import com.video.app.dto.comment.CreateCommentDto;
import com.video.app.entities.Comment;
import com.video.app.utils.DataResponse;

import java.util.List;

public interface CommentService {
    DataResponse add(String username,CreateCommentDto createCommentDto);
    List<Comment> getCommentsByVideoId(Long videoId);
}
