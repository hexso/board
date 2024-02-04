package com.example.firstproject.service;

import com.example.firstproject.data.TsRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@Service
public class VideoService{
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
}
