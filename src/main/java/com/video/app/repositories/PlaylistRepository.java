package com.video.app.repositories;

import com.video.app.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p WHERE p.user.username = :username AND p.deleted = :deleted")
    List<Playlist> findByUsernameAndDeleted(@Param("username") String username, @Param("deleted") boolean deleted);

    @Query("SELECT p FROM Playlist p WHERE p.user.username = :username AND p.id = :id")
    Playlist findByUsernameAndId(@Param("username") String username, @Param("id") Long id);
}
