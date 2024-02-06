package com.example.firstproject.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class ArticleForm {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드
    private MultipartFile file; //파일경로

    public Article toEntity() {
        return new Article(id, title, content);
    }
}
