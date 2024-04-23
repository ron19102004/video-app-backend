package com.video.app.services;

import com.video.app.dto.country.CreateCountryDto;
import com.video.app.dto.country.UpdateCountryDto;
import com.video.app.entities.Country;
import com.video.app.utils.DataResponse;

import java.util.List;

public interface CountryService {
    Country create(CreateCountryDto createCountryDto);

    List<Country> find();

    Country update(Long id, UpdateCountryDto updateCountryDto);
    DataResponse deleted(Long id);
}
