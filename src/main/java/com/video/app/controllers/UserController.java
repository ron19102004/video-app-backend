package com.video.app.controllers;

import com.video.app.dto.user.ChangeInfoUserDto;
import com.video.app.dto.user.ChangePasswordDto;
import com.video.app.dto.user.InfoUserResponseDto;
import com.video.app.entities.User;
import com.video.app.entities.VIP;
import com.video.app.exceptions.ServiceException;
import com.video.app.services.UserService;
import com.video.app.services.VIPService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SecurityUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VIPService vipService;

    @GetMapping("/info-confirmed/{id}")
    public ResponseEntity<DataResponse> infoConfirmed(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.userService.infoConfirmed(id));
    }
    @GetMapping("/info")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> info() {
        Authentication authentication = SecurityUtil.authentication();
        String username = authentication.getName();
        User user = this.userService.findByUsername(username);
        VIP vip = this.vipService.findLatestVIPBYUsername(authentication.getName());
        if (vip != null && vip.getActive() && this.vipService.isExpired(vip.getExpiredAt())) {
            vip = this.vipService.cancel(vip);
        }
        return ResponseEntity.ok(
                user != null ? new DataResponse("Info", new InfoUserResponseDto(vip, user), true) :
                        new DataResponse("Data is empty!", null, false)
        );
    }

    @PatchMapping("/image")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> updateImage(@RequestParam("file") @NotNull MultipartFile file) {
        Authentication authentication = SecurityUtil.authentication();
        String username = authentication.getName();
        return ResponseEntity.ok(this.userService.updateImage(username, file));
    }

    @PostMapping("/vip/register")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> registerVIP(@RequestParam("month") @NotNull int month) {
        Authentication authentication = SecurityUtil.authentication();
        String username = authentication.getName();
        return ResponseEntity.ok(this.vipService.register(username, month));
    }

    @DeleteMapping("/vip/cancel")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<DataResponse> cancelVIP() {
        Authentication authentication = SecurityUtil.authentication();
        String username = authentication.getName();
        return ResponseEntity.ok(this.vipService.cancel(username));
    }

    @PatchMapping("/change-info")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> changeInfo(@RequestBody ChangeInfoUserDto changeInfoUserDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.userService.changeInfo(authentication.getName(), changeInfoUserDto));
    }

    @PatchMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<DataResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        Authentication authentication = SecurityUtil.authentication();
        return ResponseEntity.ok(this.userService.changePassword(authentication.getName(), changePasswordDto));
    }

}
