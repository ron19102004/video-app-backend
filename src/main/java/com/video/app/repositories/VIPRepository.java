package com.video.app.repositories;

import com.video.app.entities.VIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VIPRepository extends JpaRepository<VIP, Long> {
    @Query("select v from VIP v where v.user.id = :userId order by v.id desc")
    List<VIP> findByUserId(@Param("userId") Long userId);

    @Query("select v from VIP v where v.user.username = :username order by v.id desc")
    List<VIP> findByUsername(@Param("username") String username);

    @Query("select v from VIP v where v.user.username = :username and v.active = :active")
    List<VIP> findByUserUsernameAndActive(@Param("username") String username, @Param("active") Boolean active);
}
