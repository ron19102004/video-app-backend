package com.video.app.repositories;

import com.video.app.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCheckedOrderByIdDesc(Boolean checked);

    Report findByIdAndChecked(Long id, Boolean checked);
}
