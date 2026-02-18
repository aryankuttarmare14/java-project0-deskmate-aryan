package com.deskmate.dao;

import com.deskmate.model.report.DailyRevenueRow;
import com.deskmate.model.report.DeskUtilizationRow;
import java.time.LocalDate;
import java.util.List;

public interface ReportDao {
	DailyRevenueRow dailyRevenue(LocalDate date);

	List<DeskUtilizationRow> deskUtilization(LocalDate date);
}