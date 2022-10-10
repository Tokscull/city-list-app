package com.github.tokscull.backend.controller;

import com.github.tokscull.backend.exeption.FileStorageException;
import com.github.tokscull.backend.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final FileStorageService fileStorageService;

    /**
     * Get image by filename
     *
     * @param filename the image filename
     * @return the ResponseEntity with status 200 (OK) and the image in body
     * @throws FileStorageException when the image fails to load
     */
    @GetMapping("/{filename}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String filename) {
        log.info("Received request to get image by filename: {}", filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(fileStorageService.loadFile(filename)));
    }

}
