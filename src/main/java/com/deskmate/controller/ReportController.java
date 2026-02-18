package com.deskmate.controller;

import com.deskmate.model.report.DeskUtilizationRow;
import com.deskmate.service.ReportService;
import com.deskmate.util.DateUtil;
import com.deskmate.util.InputUtil;

import java.time.LocalDate;

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public void menu() {

        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. Daily revenue summary (date)");
            System.out.println("2. Desk utilization (date)");
            System.out.println("0. Back");

            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> dailyRevenue();
                case 2 -> deskUtil();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void dailyRevenue() {
        LocalDate date = DateUtil.parseDate(
                InputUtil.readString("date (yyyy-MM-dd): ")
        );

        var row = reportService.dailyRevenue(date);

        System.out.println("Paid bookings: " + row.getPaidBookings());
        System.out.println("Total revenue: " + row.getTotalRevenue());
    }

    private void deskUtil() {
        LocalDate date = DateUtil.parseDate(
                InputUtil.readString("date (yyyy-MM-dd): ")
        );

        for (DeskUtilizationRow row : reportService.deskUtilization(date)) {
            System.out.println(row.getDeskCode() + " -> " +
                    row.getPaidBookingsCount());
        }
    }
}
