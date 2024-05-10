package com.video.app.controllers;

import com.video.app.dto.playlist.CreatePlaylistDto;
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> add(@RequestBody CreatePlaylistDto createPlaylistDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse(
                "Created playlist!",
                this.playlistService.add(authentication.getName(), createPlaylistDto),
                true)
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> getMyPlaylist() {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse("Got!", this.playlistService.getMyPlaylist(authentication.getName()), true));
    }

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> delete(@RequestParam("id") Long id) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.playlistService.delete(authentication.getName(), id));
    }

    @GetMapping("/videos")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> getVideosPlaylist(@RequestParam("playlistId") Long playlistId) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(new DataResponse("Data video of playlist",
                this.playlistVideoService.findByPlaylistId(authentication.getName(), playlistId),
                true));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> addVideoPlaylist(@RequestParam("playlistId") Long playlistId,
                                                         @RequestParam("videoId") Long videoId) {
        Authentication authentication = SecurityUtil.authentication();
        this.playlistVideoService.add(authentication.getName(), playlistId, videoId);
        return ResponseEntity.ok(new DataResponse("Added!", null, true));
    }

    @DeleteMapping("/video")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> deleteVideoPlaylist(@RequestParam("videoPlaylistId") Long playlistVideoId) {
        Authentication authentication = SecurityUtil.authentication();
        this.playlistVideoService.delete(authentication.getName(), playlistVideoId);
        return ResponseEntity.ok(new DataResponse("Deleted!", null, true));
    }
}
