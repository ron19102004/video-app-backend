package com.video.app.repositories;

import com.video.app.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByIdAndDeleted(Long id, Boolean deleted);
    List<Country> findByDeleted(Boolean deleted);
}
