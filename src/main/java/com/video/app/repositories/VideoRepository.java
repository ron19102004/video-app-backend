package com.video.app.repositories;

import com.video.app.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v FROM Video v WHERE v.category.id = :categoryId ORDER BY v.id desc")
    List<Video> findAllByCategoryId(@Param("categoryId") Long id);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId ORDER BY v.id desc")
    List<Video> findAllByCountryId(@Param("countryId") Long id);

    @Query("SELECT v FROM Video v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY v.id DESC")
    List<Video> findAllByNameLike(@Param("name") String name);

    @Query("SELECT v FROM Video v WHERE v.category.id = :categoryId AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY v.id desc")
    List<Video> findAllByCategoryIdAndNameLike(@Param("categoryId") Long id, @Param("name") String name);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY v.id desc")
    List<Video> findAllByCountryIdAndNameLike(@Param("countryId") Long id, @Param("name") String name);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.category.id = :categoryId ORDER BY v.id desc")
    List<Video> findAllByCountryIdAndCategoryId(@Param("countryId") Long countryId, @Param("categoryId") Long categoryId);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.category.id = :categoryId AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY v.id DESC")
    List<Video> findAllByCountryIdAndCategoryIdAndNameLike(@Param("countryId") Long countryId, @Param("categoryId") Long categoryId, @Param("name") String name);

    @Query("SELECT v FROM Video v WHERE v.uploader.id = :uploaderId ORDER BY v.id DESC")
    Page<Video> findAllByUploaderId(@Param("uploaderId") Long uploaderId, Pageable pageable);

}
