package com.github.tokscull.backend.controller;

import com.github.tokscull.backend.exeption.EntityNotFoundException;
import com.github.tokscull.backend.exeption.FileStorageException;
import com.github.tokscull.backend.model.City;
import com.github.tokscull.backend.model.rest.SearchRequest;
import com.github.tokscull.backend.service.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    /**
     * Get city by name
     *
     * @param name the City name which the requested entity should match
     * @return the ResponseEntity with status 200 (OK) and the City in body
     * @throws EntityNotFoundException when city with name not exist
     */
    @GetMapping(params = "name")
    public ResponseEntity<City> getCityByName(@RequestParam(value = "name") String name) {
        log.info("Received request to get city by name: {}", name);
        return ResponseEntity.ok().body(cityService.getCityByName(name));
    }

    /**
     * Search cities
     *
     * @param request the SearchRequest wth page, size and filter info
     * @return the ResponseEntity with status 200 (OK) and the Page of City in body
     */
    @PostMapping("/search")
    public ResponseEntity<Page<City>> searchCities(@RequestBody SearchRequest request) {
        log.info("Received request to search cities");
        return ResponseEntity.ok().body(cityService.searchCities(request));
    }

    /**
     * Update city by city id
     *
     * @param id    the City id which the requested entity should match
     * @param city  the object to update
     * @param image the City image file
     * @return the ResponseEntity with status 200 (OK) and the City in body
     * @throws EntityNotFoundException when city with name not exist
     * @throws FileStorageException    when the image cannot be saved
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestPart("city") City city,
                                           @RequestPart("image") MultipartFile image) {
        log.info("Received request to update city by id: {}", id);
        return ResponseEntity.ok().body(cityService.updateCityById(id, city, image));
    }

}
