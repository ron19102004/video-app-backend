package com.video.app.services.impl;

import com.video.app.entities.Playlist;
import com.video.app.entities.PlaylistVideo;
import com.video.app.entities.Video;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.PlaylistRepository;
import com.video.app.repositories.PlaylistVideoRepository;
import com.video.app.repositories.VideoRepository;
import com.video.app.services.PlaylistVideoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PlaylistVideoServiceImpl implements PlaylistVideoService {
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(String username, Long playlistId, Long videoId) {
        Playlist playlist = this.playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundEntity("Playlist not found"));
        if (!playlist.getUser().getUsername().equals(username))
            throw new ServiceException("Not Permission", HttpStatus.FORBIDDEN);
        Video video = this.videoRepository.findById(videoId).orElseThrow(() -> new NotFoundEntity("Playlist not found"));
        PlaylistVideo plExist = this.playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId);
        if (plExist != null) throw new ServiceException("Video absolutely added !!!",HttpStatus.ACCEPTED);
        PlaylistVideo playlistVideo = PlaylistVideo.builder()
                .playlist(playlist)
                .video(video)
                .build();
        playlist.setImage(video.getImage());
        this.playlistVideoRepository.save(playlistVideo);
        this.entityManager.merge(playlist);
    }

    @Override
    public void delete(String username, Long playlistVideoId) {
        PlaylistVideo playlistVideo = this.playlistVideoRepository.findById(playlistVideoId).orElseThrow(() -> new NotFoundEntity("Playlist Video Not Found"));
        if (!playlistVideo.getPlaylist().getUser().getUsername().equals(username))
            throw new ServiceException("Not Permission", HttpStatus.FORBIDDEN);
        this.playlistVideoRepository.delete(playlistVideo);
    }

    @Override
    public List<Video> findByPlaylistId(String username, Long playlistId) {
        Playlist playlist = this.playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundEntity("Playlist not found"));
        if (!playlist.getUser().getUsername().equals(username))
            throw new ServiceException("Not Permission", HttpStatus.FORBIDDEN);
        return this.playlistVideoRepository.findAllVideoByPlaylistId(playlistId);
    }

}
