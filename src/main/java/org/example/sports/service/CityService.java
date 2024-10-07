package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.model.City;
import org.example.sports.repositore.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;

    public City createCity(CreateCity cityDto) {
        City city = City.builder()
                .name(cityDto.name())
                .region(cityDto.region())
                .build();

        return cityRepository.save(city);
    }

    public Page<City> findAllCities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cityRepository.findAll(pageable);
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
    }

    public long countCities() {
        return cityRepository.count();
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
