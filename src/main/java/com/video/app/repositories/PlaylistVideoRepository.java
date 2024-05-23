package com.video.app.repositories;

import com.video.app.entities.PlaylistVideo;
import com.video.app.entities.Privacy;
import com.video.app.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, Long> {
    @Query("SELECT pl.video FROM PlaylistVideo pl WHERE pl.playlist.id = :id ORDER BY pl.id DESC")
    List<Video> findAllVideoByPlaylistId(@Param("id") Long id);

    @Query("SELECT pl.video FROM PlaylistVideo pl WHERE pl.playlist.id = :id AND pl.playlist.privacy = :privacy ORDER BY pl.id DESC")
    List<Video> findAllVideoByPlaylistIdAndPrivacy(@Param("id") Long id, @Param("privacy") Privacy privacy);


    @Query("SELECT pl FROM PlaylistVideo pl WHERE pl.playlist.id = :playlistId AND pl.video.id = :videoId")
    PlaylistVideo findByPlaylistIdAndVideoId(@Param("playlistId") Long playlistId,
                                             @Param("videoId") Long videoId);
}
