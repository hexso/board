package com.example.firstproject.repository;

import com.example.firstproject.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Videos, Long> {
}
