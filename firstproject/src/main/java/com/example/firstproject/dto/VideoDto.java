package com.example.firstproject.dto;

import com.example.firstproject.entity.Comment;
import com.example.firstproject.entity.Videos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class VideoDto {
    private Long id;
    private Long articleId;
    private String path;
    private String title;
    private String videoLink;

    public static VideoDto createVideoDto(Videos videos) {
        return new VideoDto(
                videos.getId(),
                videos.getArticle_id(),
                videos.getPath(),
                videos.getTitle(),
                "http://localhost:8080/video/"+videos.getTitle()+"/index.m3u8"
        );
    }
}
