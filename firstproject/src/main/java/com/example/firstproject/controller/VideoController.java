package com.example.firstproject.controller;

import com.example.firstproject.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

@Slf4j
@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/video/sample")
    public ResponseEntity<StreamingResponseBody> video() {
        UUID uuid = new UUID(1,2);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/vnd.apple.mpegurl");
        httpHeaders.set("Content-Disposition", "attachment;filename=index.m3u8");
        try {
            StreamingResponseBody body = videoService.m3u8Index(uuid);
            return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
        } catch( java.io.FileNotFoundException e) {
            log.info("asdasd");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/video/{id}.ts")
    public ResponseEntity<StreamingResponseBody> ts(@PathVariable String id) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/vnd.apple.mpegurl");
            httpHeaders.set("Content-Disposition", "attachment;filename=" + id + ".ts");
            StreamingResponseBody body = videoService.ts(id + ".ts");
            return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
