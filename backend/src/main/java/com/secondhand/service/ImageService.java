package com.secondhand.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "uploads/";

    public String saveImage(MultipartFile file) {

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        }
        catch (IOException e) {

            throw new RuntimeException("Cannot save image");

        }

    }

    public void deleteImage(String fileName) {
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.deleteIfExists(path);
        }
        catch (IOException e) {

            throw new RuntimeException("Cannot delete image");

        }
    }

}