package org.example.sports.controller.city;

import lombok.RequiredArgsConstructor;
import org.example.sports.controller.city.dto.CityDto;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;


    @PostMapping("/change")
    public ResponseEntity<CityDto> createCity(@RequestBody CreateCity createCity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(createCity));
    }

    @GetMapping
    public ResponseEntity<Page<CityDto>> getAllCities(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CityDto> cities = cityService.findAllCities(page, size);
        long totalCities = cityService.countCities();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(totalCities));

        return new ResponseEntity<>(cities, headers, HttpStatus.OK);
    }


    @DeleteMapping("/change/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

}
