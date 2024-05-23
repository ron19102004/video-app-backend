package com.video.app.services.impl;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.video.app.dto.playlist.CreatePlaylistDto;
import com.video.app.entities.Playlist;
import com.video.app.entities.Privacy;
import com.video.app.entities.User;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.repositories.PlaylistRepository;
import com.video.app.services.PlaylistService;
import com.video.app.services.UserService;
import com.video.app.utils.DataResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Playlist> getMyPlaylist(String username) {
        return this.playlistRepository.findByUsernameAndDeleted(username, false);
    }

    @Override
    public Playlist add(String username, CreatePlaylistDto createPlaylistDto) {
        User user = this.userService.findByUsername(username);
        Playlist playlist = Playlist.builder()
                .privacy(createPlaylistDto.isPublic() ? Privacy.PUBLIC : Privacy.PRIVATE)
                .name(createPlaylistDto.name())
                .image("")
                .deleted(false)
                .user(user)
                .build();
        return this.playlistRepository.save(playlist);
    }

    @Override
    public DataResponse delete(String username, Long idPlaylist) {
        Playlist playlist = this.playlistRepository.findByUsernameAndId(username, idPlaylist);
        if (playlist == null) throw new NotFoundEntity("Playlist not found");
        playlist.setDeleted(true);
        this.entityManager.merge(playlist);
        return new DataResponse("Deleted!", null, true);
    }

    @Override
    public Playlist findById(Long id) {
        return this.playlistRepository.findById(id).orElseThrow(()->new NotFoundEntity("Playlist not found"));
    }

    @Override
    public List<Playlist> getUserConfirmedPlaylist(Long id) {
        return this.playlistRepository.findByUserIdAndDeletedAndPrivacy(id,false,Privacy.PUBLIC);
    }
}
