package com.video.app.repositories;

import com.video.app.entities.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    @Query("SELECT s FROM Subscribe s WHERE subscribe.username = :usernameOfSubscribe AND subscribed.id = :idOfSubscribed")
    Subscribe findBySubscribeUsernameAndSubscribedId(@Param("usernameOfSubscribe") String username, @Param("idOfSubscribed") Long id);
    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.subscribed.id = :userId")
    Long countSubscribed(@Param("userId") Long userId);
    @Query("SELECT COUNT(s) FROM Subscribe s WHERE s.subscribe.id = :userId")
    Long countSubscribing(@Param("userId") Long userId);
}
