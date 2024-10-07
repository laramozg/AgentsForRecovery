package org.example.sports.mapper;

import org.example.sports.controller.city.dto.CityDto;
import org.example.sports.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {
    public CityDto convertToDto(City city) {
        return new CityDto(
                city.getId(),
                city.getName(),
                city.getRegion());
    }

}
