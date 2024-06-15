package com.video.app.services;

import com.video.app.dto.subscribe.SubcribeInfoDto;
import com.video.app.entities.Subscribe;

public interface SubscribeService {
    Subscribe subscribe(String usernameOfUserSubscribe, Long idOfUserBeSubscribed);

    void unsubscribe(String usernameOfUserSubscribe, Long idOfUserBeSubscribed);

    Subscribe isValidSubscribed(String usernameOfUserSubscribe, Long idOfUserBeSubscribed);
    SubcribeInfoDto getSubscribe(Long userId);
}
