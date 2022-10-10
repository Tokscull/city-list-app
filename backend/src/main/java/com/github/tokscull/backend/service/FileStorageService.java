package com.github.tokscull.backend.service;

import com.github.tokscull.backend.exeption.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;

@Service
public class FileStorageService {

    @Value("${file.storage.location}")
    private Path rootLocation;

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }

        String filename = Instant.now().getEpochSecond() + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + filename);
        }
    }

    public void removeFile(String filename) {
        Path location = this.rootLocation.resolve(filename).normalize();

        try {
            Files.deleteIfExists(location);
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file " + filename);
        }
    }

    public InputStream loadFile(String filename) {
        Path location = this.rootLocation.resolve(filename).normalize();

        try {
            Resource resource = new UrlResource(location.toUri());
            return resource.getInputStream();
        } catch (IOException e) {
            throw new FileStorageException("Could not load file " + filename);
        }
    }

}
