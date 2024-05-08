package com.video.app.controllers;

import com.video.app.dto.playlist.CreatePlaylistDto;
import com.video.app.services.PlaylistService;
import com.video.app.utils.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> add(@RequestBody CreatePlaylistDto createPlaylistDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated or missing token", null, false));
        }
        return ResponseEntity.ok(new DataResponse(
                "Created playlist!",
                this.playlistService.add(authentication.getName(), createPlaylistDto),
                true)
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> getMyPlaylist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated or missing token", null, false));
        }
        return ResponseEntity.ok(new DataResponse("Got!", this.playlistService.getMyPlaylist(authentication.getName()), true));
    }

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> delete(@RequestParam("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated or missing token", null, false));
        }
        return ResponseEntity.ok(this.playlistService.delete(authentication.getName(), id));
    }
}
