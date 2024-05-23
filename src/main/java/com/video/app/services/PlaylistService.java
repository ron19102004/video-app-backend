package com.video.app.services;

import com.video.app.dto.playlist.CreatePlaylistDto;
import com.video.app.entities.Playlist;
import com.video.app.utils.DataResponse;

import java.util.List;

public interface PlaylistService {
    List<Playlist> getMyPlaylist(String username);
    Playlist add(String username, CreatePlaylistDto createPlaylistDto);
    DataResponse delete(String username,Long idPlaylist);
    Playlist findById(Long id);
    List<Playlist> getUserConfirmedPlaylist(Long id);
}
