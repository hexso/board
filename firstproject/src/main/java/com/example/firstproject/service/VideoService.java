package com.example.firstproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.UUID;

@Service
public class VideoService{
    public StreamingResponseBody m3u8Index(UUID uuid) throws java.io.FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\kesu9\\OneDrive\\바탕 화면\\ffmpeg-6.1.1\\bin\\index.m3u");
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

    public StreamingResponseBody ts(String s) throws java.io.FileNotFoundException {

        InputStream inputStream = new FileInputStream("C:\\Users\\kesu9\\OneDrive\\바탕 화면\\ffmpeg-6.1.1\\bin\\" + s);
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
}
