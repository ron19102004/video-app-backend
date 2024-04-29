package com.video.app.repositories;

import com.video.app.entities.VIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VIPRepository extends JpaRepository<VIP, Long> {
    @Query("select v from VIP v where v.user.id = :userId order by v.id desc")
    VIP findLatestVIPByUserId(Long userId);

    @Query("select v from VIP v where v.user.username = :username order by v.id desc")
    VIP findLatestVIPByUsername(String username);
}
