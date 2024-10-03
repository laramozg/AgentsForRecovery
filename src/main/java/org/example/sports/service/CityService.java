package org.example.sports.service;

import lombok.RequiredArgsConstructor;
import org.example.sports.controller.city.dto.CityDto;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.mapper.CityMapper;
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
    private final CityMapper cityMapper;

    public CityDto createCity(CreateCity cityDto) {
        City city = City.builder()
                .name(cityDto.name())
                .region(cityDto.region())
                .build();

        City sreatedCity = cityRepository.save(city);
        return cityMapper.toDto(sreatedCity);
    }

    public Page<CityDto> findAllCities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cityRepository.findAll(pageable)
                .map(cityMapper::toDto);
    }

    public long countCities() {
        return cityRepository.count();
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
