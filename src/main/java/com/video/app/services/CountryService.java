package com.video.app.services;

import com.video.app.dto.country.CreateCountryDto;
import com.video.app.dto.country.UpdateCountryDto;
import com.video.app.entities.Country;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.exceptions.ServiceException;
import com.video.app.utils.DataResponse;

import java.util.List;

public interface CountryService {
    Country create(CreateCountryDto createCountryDto);

    List<Country> find();

    Country update(Long id, UpdateCountryDto updateCountryDto) throws NotFoundEntity;

    DataResponse delete(Long id) throws NotFoundEntity;
    Country findById(Long id);
}
