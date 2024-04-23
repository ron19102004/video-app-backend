package com.video.app.controllers;

import com.video.app.dto.report.CreateReportDto;
import com.video.app.entities.Report;
import com.video.app.exceptions.GlobalException;
import com.video.app.services.ReportService;
import com.video.app.utils.DataResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController extends GlobalException {
    @Autowired
    private ReportService reportService;

    @PostMapping("/new")
    public ResponseEntity<DataResponse> create(@NotNull @RequestBody CreateReportDto createReportDto) {
        return ResponseEntity.ok(this.reportService.create(createReportDto));
    }

    @GetMapping("/all-checked")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Report>> getAllChecked() {
        return ResponseEntity.ok(this.reportService.reportsChecked());
    }

    @GetMapping("/all-checked-yet")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Report>> getAllCheckedYet() {
        return ResponseEntity.ok(this.reportService.reportsCheckedYet());
    }

    @PostMapping("/{id}/handle")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DataResponse> handleReport(
            @NotNull @PathVariable("id") Long id,
            @NotNull @RequestParam("reply") String reply
    ) {
        return ResponseEntity.ok(this.reportService.check(id, reply));
    }
}
