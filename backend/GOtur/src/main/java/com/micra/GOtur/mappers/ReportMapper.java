package com.micra.GOtur.mappers;

import com.micra.GOtur.models.Report;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReportMapper implements RowMapper<Report> {

    @Override
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {

        int report_id = rs.getInt("report_id");
        int admin_id = rs.getInt("admin_id");
        String details = rs.getString("details");
        String report_type = rs.getString("report_type");
        LocalDate report_date = rs.getDate("report_date").toLocalDate();

        Report report = new Report();
        report.setReport_id(report_id);
        report.setAdmin_id(admin_id);
        report.setDetails(details);
        report.setReport_type(report_type);
        report.setReport_date(report_date);

        return report;
    }
}
