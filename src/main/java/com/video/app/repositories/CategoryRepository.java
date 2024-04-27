package com.video.app.repositories;

import com.video.app.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeleted(Long id, Boolean deleted);
    List<Category> findByDeleted(Boolean deleted);
}
