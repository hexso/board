package com.example.firstproject.service;

import com.example.firstproject.data.TsRequest;
import com.example.firstproject.dto.VideoDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Videos;
import com.example.firstproject.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService{
    @Autowired
    private VideoRepository videoRepository;

    public List<VideoDto> videos(Long articleId) {
        return videoRepository.findByArticleId(articleId)
                .stream()
                .map(videos -> VideoDto.createVideoDto(videos))
                .collect(Collectors.toList());
    }

    public StreamingResponseBody m3u8Index(Long id) throws java.io.FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("D:\\new\\" + id + "\\index.m3u8");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        return outputStream -> {
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //if (line.endsWith(".ts")) line = tsPrefix + line;
                    outputStream.write(line.getBytes());
                    outputStream.write(System.lineSeparator().getBytes());
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bufferedReader.close();
                outputStream.close();
            }
        };
    }

    public StreamingResponseBody ts(TsRequest request) throws java.io.FileNotFoundException {

        InputStream inputStream = new FileInputStream("D:\\new\\" + request.getId() + "\\" + request.getTs());
        return outputStream -> {
            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
                outputStream.close();
            }
        };
    }

    // 파일명에서 확장자를 제외한 부분 추출하는 메서드
    private static String getFolderName(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex != -1) {
            return filename.substring(0, lastIndex);
        } else {
            return filename; // 확장자가 없는 경우 그대로 반환
        }
    }

    public boolean changeTom3u8(String filePath) {

        Path folderPath = Paths.get(getFolderName(filePath));
        log.info(folderPath.toString());
        try {
            // 폴더를 생성할 경로를 Path 객체로 변환
            // 폴더 생성
            Files.createDirectories(folderPath);
            log.info("폴더가 생성되었습니다.");
        } catch (Exception e) {
            log.info("폴더 생성 중 오류가 발생했습니다.");
            e.printStackTrace();
            return false;
        }

        String ffmpegCmd = "ffmpeg -i \"" + filePath + "\" -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls \"" + folderPath.toString() + "\\index.m3u8\"";
        log.info(ffmpegCmd);
        try {
            // 명령어 실행
            Process process = Runtime.getRuntime().exec(ffmpegCmd);

            // 실행 결과 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            log.info("FFmpeg 실행 종료 코드: " + exitCode);
        } catch (IOException | InterruptedException e) {
            log.info(e.toString());
            return false;
        }
        log.info("완료되었습니다.");
        return true;
    }

    public void save_file(MultipartFile videoFile, Article savedArticle) {
        String uploadDir = "D:/new";
        if (videoFile.isEmpty()) {
            log.info("파일이 없습니다.");
            return ;
        }
        try {
            // 파일 이름 생성
            //확장자 추출
            String originalFilename = videoFile.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                int lastIndex = originalFilename.lastIndexOf('.');
                if (lastIndex != -1) {
                    extension = originalFilename.substring(lastIndex + 1);
                }
            }

            String fileName = savedArticle.getTitle() + "." +extension;
            // 파일 저장 경로 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(videoFile.getInputStream(), filePath);

            // 파일 저장 성공 시 추가 작업 수행 가능
            Videos videos = new Videos(null, savedArticle.getId(), filePath.toString(), savedArticle.getTitle());
            videoRepository.save(videos);
            changeTom3u8(filePath.toString());
            log.info("파일이 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            log.info(e.toString());
            log.info("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}
