package com.video.app.repositories;

import com.video.app.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.video.id = :videoId AND c.parentComment IS NULL ORDER BY c.id DESC")
    List<Comment> findAllByVideoId(@Param("videoId") Long videoId);
}
