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

            String originalName = file.getOriginalFilename();

            if (originalName == null) {
                throw new RuntimeException("Invalid file name");
            }
            
            originalName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");

            String fileName = UUID.randomUUID() + "_" + originalName;

            Path path = Paths.get(UPLOAD_DIR, fileName);

            Files.createDirectories(path.getParent());

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {

            throw new RuntimeException("Cannot save image", e);

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