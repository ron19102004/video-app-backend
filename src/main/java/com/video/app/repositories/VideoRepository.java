package com.video.app.repositories;

import com.video.app.entities.Privacy;
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
    @Query("SELECT v FROM Video v WHERE v.deleted = :deleted AND v.privacy = :privacy ORDER BY v.id desc")
    Page<Video> findAllWithPage(@Param("deleted") Boolean deleted, Pageable pageable, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.category.id = :categoryId AND v.deleted = :deleted AND v.privacy = :privacy ORDER BY v.id desc")
    List<Video> findAllByCategoryId(@Param("categoryId") Long id, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.deleted = :deleted AND v.privacy = :privacy ORDER BY v.id desc")
    List<Video> findAllByCountryId(@Param("countryId") Long id, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) AND v.privacy = :privacy AND v.deleted = :deleted ORDER BY v.id DESC")
    List<Video> findAllByNameLike(@Param("name") String name, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.category.id = :categoryId AND v.privacy = :privacy AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) AND v.deleted = :deleted ORDER BY v.id desc")
    List<Video> findAllByCategoryIdAndNameLike(@Param("categoryId") Long id, @Param("name") String name, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.privacy = :privacy AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) AND v.deleted = :deleted ORDER BY v.id desc")
    List<Video> findAllByCountryIdAndNameLike(@Param("countryId") Long id, @Param("name") String name, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.privacy = :privacy AND v.category.id = :categoryId AND v.deleted = :deleted ORDER BY v.id desc")
    List<Video> findAllByCountryIdAndCategoryId(@Param("countryId") Long countryId, @Param("categoryId") Long categoryId, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.country.id = :countryId AND v.privacy = :privacy AND v.category.id = :categoryId AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')) AND v.deleted = :deleted ORDER BY v.id DESC")
    List<Video> findAllByCountryIdAndCategoryIdAndNameLike(@Param("countryId") Long countryId, @Param("categoryId") Long categoryId, @Param("name") String name, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    @Query("SELECT v FROM Video v WHERE v.uploader.id = :uploaderId AND v.privacy = :privacy AND v.deleted = :deleted ORDER BY v.id DESC")
    Page<Video> findAllByUploaderId(@Param("uploaderId") Long uploaderId, Pageable pageable, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

    Video findBySlug(String slug);

    @Query("SELECT v FROM Video v WHERE v.uploader.username = :username AND v.privacy = :privacy AND v.deleted = :deleted ORDER BY v.id DESC")
    List<Video> findAllByUsername(@Param("username") String username, @Param("deleted") Boolean deleted, @Param("privacy") Privacy privacy);

}
