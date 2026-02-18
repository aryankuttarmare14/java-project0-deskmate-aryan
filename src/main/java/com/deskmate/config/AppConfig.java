package com.deskmate.config;

import com.deskmate.controller.*;
import com.deskmate.dao.*;
import com.deskmate.dao.impl.*;
import com.deskmate.service.*;

public class AppConfig {
	public DeskController deskController() {
		return null;
	}

	public BookingController bookingController() {
		DeskDao deskDao = new JdbcDeskDao();
		BookingDao bookingDao = new JdbcBookingDao();
		PaymentDao paymentDao = new JdbcPaymentDao();
		BookingService bookingService = new BookingService(deskDao, bookingDao, paymentDao);
		return new BookingController(bookingService);
	}

	public ReportController reportController() {
		ReportDao reportDao = new JdbcReportDao();
		ReportService reportService = new ReportService(reportDao);
		return new ReportController(reportService);
	}
}