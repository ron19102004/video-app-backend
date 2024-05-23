package com.video.app.controllers;

import com.video.app.dto.playlist.CreatePlaylistDto;
import com.video.app.entities.Playlist;
import com.video.app.entities.Privacy;
import com.video.app.services.PlaylistService;
import com.video.app.services.PlaylistVideoService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistVideoService playlistVideoService;

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> add(@RequestBody CreatePlaylistDto createPlaylistDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse(
                "Created playlist!",
                this.playlistService.add(authentication.getName(), createPlaylistDto),
                true)
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> getMyPlaylist() {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse("Got!", this.playlistService.getMyPlaylist(authentication.getName()), true));
    }
    @GetMapping("/user-confirmed")
    public ResponseEntity<DataResponse> getUserConfirmedPlaylist(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(new DataResponse("Got!", this.playlistService.getUserConfirmedPlaylist(userId), true));
    }

    @DeleteMapping("")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> delete(@RequestParam("id") Long id) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.playlistService.delete(authentication.getName(), id));
    }

    @GetMapping("/videos")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> getVideosPlaylist(@RequestParam("playlistId") Long playlistId) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse("Data video of playlist",
                this.playlistVideoService.findByPlaylistId(authentication.getName(), playlistId),
                true));
    }

    @GetMapping("/public/videos")
    public ResponseEntity<DataResponse> getVideosPlaylistPublic(@RequestParam("playlistId") Long playlistId) {
        Playlist playlist = this.playlistService.findById(playlistId);
        if (playlist.getPrivacy().equals(Privacy.PRIVATE))
            return ResponseEntity.ok(new DataResponse("Not permission", null, false));
        return ResponseEntity.ok(new DataResponse("Data video of playlist",
                this.playlistVideoService.findByPlaylistPublicId(playlistId),
                true));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> addVideoPlaylist(@RequestParam("playlistId") Long playlistId,
                                                         @RequestParam("videoId") Long videoId) {
        Authentication authentication = SecurityUtil.authentication();
        this.playlistVideoService.add(authentication.getName(), playlistId, videoId);
        return ResponseEntity.ok(new DataResponse("Added!", null, true));
    }

    @DeleteMapping("/video")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> deleteVideoPlaylist(@RequestParam("videoPlaylistId") Long playlistVideoId) {
        Authentication authentication = SecurityUtil.authentication();
        this.playlistVideoService.delete(authentication.getName(), playlistVideoId);
        return ResponseEntity.ok(new DataResponse("Deleted!", null, true));
    }
}
