package com.video.app.services;

import com.video.app.entities.VIP;
import com.video.app.utils.DataResponse;

import java.util.Date;

public interface VIPService {
    VIP findLatestVIPByUserId(Long userId);
    VIP findLatestVIPBYUsername(String username);
    boolean isExpired(Date expiredAt);
    DataResponse register(String username,int month);
    VIP cancel(VIP vip);
}
