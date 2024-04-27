package com.video.app.services.impl;

import com.video.app.dto.mail.MailSendMessageDto;
import com.video.app.dto.report.CreateReportDto;
import com.video.app.entities.Report;
import com.video.app.mail.MailService;
import com.video.app.repositories.ReportRepository;
import com.video.app.services.ReportService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidationRegex;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MailService mailService;

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
        if (!ValidationRegex.isEmail(createReportDto.email()))
            return new DataResponse("Email is invalid!", null, false);
        Report report = Report
                .builder()
                .email(createReportDto.email())
                .content(createReportDto.content())
                .checked(false)
                .build();
        this.reportRepository.save(report);
        String textResponse = "When something gets reported to Video, we'll review it and take action on anything we determine doesn't follow our Community Standards";
        try {
            mailService.sendMailForReport(createReportDto.email());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new DataResponse("Report created! " + textResponse, null, true);
    }

    @Override
    public DataResponse check(Long id, String reply) {
        Report report = this.reportRepository.findByIdAndChecked(id, false);
        if (report == null) return new DataResponse("Report not found!", null, false);
        //reply handling -- HANDLED YET
        report.setChecked(true);
        this.entityManager.merge(report);
        return new DataResponse("Handled!", null, true);
    }
}
