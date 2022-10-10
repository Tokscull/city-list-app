package com.github.tokscull.backend.service;

import com.github.tokscull.backend.exeption.EntityNotFoundException;
import com.github.tokscull.backend.model.City;
import com.github.tokscull.backend.model.rest.SearchRequest;
import com.github.tokscull.backend.repository.CityRepository;
import com.github.tokscull.backend.util.SearchSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final FileStorageService fileStorageService;

    public Page<City> searchCities(SearchRequest request) {
        SearchSpecification<City> spec = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return cityRepository.findAll(spec, pageable);
    }

    public City getCityByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("City not found for name: " + name));
    }

    public City updateCityById(Long id, City city, MultipartFile image) {
        City existCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found for id: " + id));

        String oldImagePath = existCity.getImagePath();
        String newImagePath = fileStorageService.storeFile(image);

        existCity.setName(city.getName());
        existCity.setImagePath(newImagePath);
        City updatedCity = cityRepository.save(existCity);

        fileStorageService.removeFile(oldImagePath);
        return updatedCity;
    }
}
