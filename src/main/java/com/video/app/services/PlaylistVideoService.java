package com.video.app.services;

import com.video.app.entities.Video;

import java.util.List;

public interface PlaylistVideoService {
    void add(String username,Long playlistId, Long videoId);

    void delete(String username, Long playlistVideoId);
    List<Video> findByPlaylistId(String username, Long playlistId);
    List<Video> findByPlaylistPublicId(Long playlistId);
}
