package com.video.app.services.impl;

import com.video.app.dto.report.CreateReportDto;
import com.video.app.entities.Report;
import com.video.app.repositories.ReportRepository;
import com.video.app.services.ReportService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidationRegex;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional(Transactional.TxType.MANDATORY)
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Report> reportsCheckedYet() {
        return this.reportRepository.findByCheckedOrderByIdDesc(false);
    }

    @Override
    public List<Report> reportsChecked() {
        return this.reportRepository.findByCheckedOrderByIdDesc(true);
    }

    @Override
    public DataResponse create(CreateReportDto createReportDto) {
        if (!ValidationRegex.isEmail(createReportDto.getEmail()))
            return new DataResponse("Email is invalid!");
        Report report = Report
                .builder()
                .email(createReportDto.getEmail())
                .content(createReportDto.getContent())
                .checked(false)
                .build();
        this.reportRepository.save(report);
        return new DataResponse("Created!", true);
    }

    @Override
    public DataResponse check(Long id, String reply) {
        Report report = this.reportRepository.findByIdAndChecked(id, false);
        if (report == null) return new DataResponse("Report not found!");
        //reply handling -- HANDLED YET
        report.setChecked(true);
        this.entityManager.merge(report);
        return new DataResponse("Handled!", true);
    }
}
