package com.video.app.repositories;

import com.video.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByPhone(String phone);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.confirmed = :confirmed")
    User findByUserIdAndConfirmed(@Param("id") Long id, @Param("confirmed") Boolean confirmed);
}
