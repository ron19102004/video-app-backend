package com.video.app.controllers;

import com.video.app.dto.country.CreateCountryDto;
import com.video.app.dto.country.UpdateCountryDto;
import com.video.app.entities.Country;
import com.video.app.exceptions.GlobalException;
import com.video.app.services.CountryService;
import com.video.app.utils.DataResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController extends GlobalException {
    @Autowired
    private CountryService countryService;

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DataResponse> create(@NotNull @RequestBody CreateCountryDto createCountryDto) {
        return ResponseEntity.ok(DataResponse.builder()
                .data(this.countryService.create(createCountryDto))
                .message("Created!")
                .status(true)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.countryService.find());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DataResponse> update(@NotNull @PathVariable("id") Long id,
                                               @NotNull @RequestBody UpdateCountryDto updateCountryDto) {
        Country country = this.countryService.update(id, updateCountryDto);
        return ResponseEntity.ok(DataResponse.builder()
                .data(country)
                .message(country == null ? "Update Failed!" : "Updated!")
                .status(true)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DataResponse> delete(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.countryService.deleted(id));
    }
}
