package com.video.app.services.impl;

import com.video.app.dto.subscribe.SubcribeInfoDto;
import com.video.app.entities.Subscribe;
import com.video.app.entities.User;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.SubscribeRepository;
import com.video.app.repositories.UserRepository;
import com.video.app.services.SubscribeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SubscribeServiceImpl implements SubscribeService {
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Subscribe subscribe(String usernameOfUserSubscribe, Long idOfUserBeSubscribed) {
        User userSubscribe = this.userRepository.findByUsername(usernameOfUserSubscribe)
                .orElseThrow(() -> new NotFoundEntity("User-subscribe not found"));
        User userBeSubscribed = this.userRepository.findById(idOfUserBeSubscribed)
                .orElseThrow(() -> new NotFoundEntity("User-be-subscribed not found"));
        if (usernameOfUserSubscribe.equals(userBeSubscribed.getUsername()))
            throw new ServiceException("User-be-subscribe is you");
        Subscribe subscribe = Subscribe.builder()
                .subscribe(userSubscribe)
                .subscribed(userBeSubscribed)
                .build();
        return this.subscribeRepository.save(subscribe);
    }

    @Override
    public void unsubscribe(String usernameOfUserSubscribe, Long idOfUserBeSubscribed) {
        Subscribe subscribe = this.subscribeRepository.findBySubscribeUsernameAndSubscribedId(usernameOfUserSubscribe, idOfUserBeSubscribed);
        if (subscribe == null) throw new NotFoundEntity("Subscribe not found");
        this.subscribeRepository.delete(subscribe);
    }

    @Override
    public Subscribe isValidSubscribed(String usernameOfUserSubscribe, Long idOfUserBeSubscribed) {
        return this.subscribeRepository.findBySubscribeUsernameAndSubscribedId(usernameOfUserSubscribe, idOfUserBeSubscribed);
    }

    @Override
    public SubcribeInfoDto getSubscribe(Long userId) {
        Long subscribed = this.subscribeRepository.countSubscribed(userId);
        Long subscribing = this.subscribeRepository.countSubscribing(userId);
        return new SubcribeInfoDto(subscribed, subscribing);
    }
}
