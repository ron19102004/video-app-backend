package com.video.app.services.impl;

import com.video.app.dto.country.CreateCountryDto;
import com.video.app.dto.country.UpdateCountryDto;
import com.video.app.entities.Country;
import com.video.app.exceptions.NotFoundEntity;
import com.video.app.exceptions.ServiceException;
import com.video.app.repositories.CountryRepository;
import com.video.app.services.CountryService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.SlugUtils;
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
                .slug(SlugUtils.slugify(createCountryDto.name()))
                .build());
    }

    @Override
    public List<Country> find() {
        return this.countryRepository.findByDeleted(false);
    }

    @Override
    public Country update(Long id, UpdateCountryDto updateCountryDto) {
        Country country = this
                .countryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundEntity("Country not found"));
        country.setName(updateCountryDto.name());
        country.setSlug(SlugUtils.slugify(updateCountryDto.name()));
        return this.entityManager.merge(country);
    }

    @Override
    public DataResponse delete(Long id) {
        Country country = this
                .countryRepository
                .findByIdAndDeleted(id, false)
                .orElseThrow(() -> new NotFoundEntity("Country not found"));
        country.setDeleted(true);
        this.entityManager.merge(country);
        return new DataResponse("Deleted!", null, true);
    }

    @Override
    public Country findById(Long id) {
        return this.countryRepository.findById(id).orElseThrow(() -> new NotFoundEntity("Country not found"));
    }
}
