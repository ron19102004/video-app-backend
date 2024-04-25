package com.video.app.services.impl;

import com.video.app.dto.country.CreateCountryDto;
import com.video.app.dto.country.UpdateCountryDto;
import com.video.app.entities.Country;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.CountryRepository;
import com.video.app.services.CountryService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidString;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Country create(CreateCountryDto createCountryDto) {
        return this.countryRepository.save(Country
                .builder()
                .name(createCountryDto.name())
                .deleted(false)
                .slug(ValidString.slugify(createCountryDto.name()))
                .build());
    }

    @Override
    public List<Country> find() {
        return this.countryRepository.findAll();
    }

    @Override
    public Country update(Long id, UpdateCountryDto updateCountryDto) {
        Country country = this
                .countryRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException("Country not found"));
        country.setName(updateCountryDto.name());
        country.setSlug(ValidString.slugify(updateCountryDto.name()));
        return this.entityManager.merge(country);
    }

    @Override
    public DataResponse delete(Long id) {
        Country country = this
                .countryRepository
                .findByIdAndDeleted(id, false)
                .orElseThrow(() -> new ServiceException("Country not found"));
        country.setDeleted(true);
        this.entityManager.merge(country);
        return new DataResponse("Deleted!", null,true);
    }
}
