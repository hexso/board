package com.example.firstproject.repository;

import com.example.firstproject.dto.VideoDto;
import com.example.firstproject.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Videos, Long> {
    @Query(value = "SELECT * FROM videos WHERE article_id = :articleId",
            nativeQuery = true)
    List<Videos> findByArticleId(Long articleId);
}
