package com.example.firstproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.util.UUID;

@Service
public class VideoService {

    @Transactional
    public StreamingResponseBody m3u8Index(UUID videoId) throws FileNotFoundException {
        return outputStream -> {
            String line;
        };
    }
}
