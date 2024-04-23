package com.video.app.services;

import com.video.app.dto.report.CreateReportDto;
import com.video.app.entities.Report;
import com.video.app.utils.DataResponse;

import java.util.List;

public interface ReportService {
    List<Report> reportsCheckedYet();

    List<Report> reportsChecked();

    DataResponse create(CreateReportDto createReportDto);

    DataResponse check(Long id, String reply);
}
