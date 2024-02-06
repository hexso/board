package com.example.firstproject.controller;

import com.example.firstproject.data.TsRequest;
import com.example.firstproject.dto.VideoDto;
import com.example.firstproject.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/video/{id}/index.m3u8")
    public ResponseEntity<StreamingResponseBody> video(@PathVariable Long id) {
        //UUID uuid = new UUID(1,2);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/vnd.apple.mpegurl");
        httpHeaders.set("Content-Disposition", "attachment;filename=index.m3u8");
        try {
            StreamingResponseBody body = videoService.m3u8Index(id);
            return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
        } catch( java.io.FileNotFoundException e) {
            log.info("asdasd");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/video/{id}/{ts}")
    public ResponseEntity<StreamingResponseBody> ts(@PathVariable String id,
                                                    @PathVariable String ts) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/vnd.apple.mpegurl");
            httpHeaders.set("Content-Disposition", "attachment;filename=" + ts);
            StreamingResponseBody body = videoService.ts(new TsRequest(id, ts));
            return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
        } catch(Exception e) {
            log.info(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/video")
    public String video_player(Model model) {
        VideoDto videoDto = new VideoDto(1l,1l,"http://localhost:8080/video/1/index.m3u8","sample1");
        model.addAttribute("videoDtos", videoDto);
        return "videos/video";
    }
}
