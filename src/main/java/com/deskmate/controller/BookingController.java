package com.deskmate.controller;

import com.deskmate.constants.PaymentMode;
import com.deskmate.service.BookingService;
import com.deskmate.utils.DateUtil;
import com.deskmate.utils.InputUtil;
import com.deskmate.utils.MoneyUtil;

import java.time.LocalDateTime;

public class BookingController {

    private final BookingService bookingService = new BookingService();

    public void createBooking() {

        long deskId = Long.parseLong(InputUtil.readString("Desk ID: "));
        String phone = InputUtil.readString("Phone (10 digits): ");

        LocalDateTime start = DateUtil.parseDateTime(
                InputUtil.readString("Start (yyyy-MM-dd HH:mm): ")
        );

        LocalDateTime end = DateUtil.parseDateTime(
                InputUtil.readString("End (yyyy-MM-dd HH:mm): ")
        );

        var total = MoneyUtil.parse(
                InputUtil.readString("Total Amount: ")
        );

        System.out.println("1) CASH  2) CARD  3) UPI");
        int choice = InputUtil.readInt("Choose payment mode: ");

        PaymentMode mode = switch (choice) {
            case 1 -> PaymentMode.CASH;
            case 2 -> PaymentMode.CARD;
            case 3 -> PaymentMode.UPI;
            default -> throw new RuntimeException("Invalid payment mode");
        };

        var paid = MoneyUtil.parse(
                InputUtil.readString("Paid Amount: ")
        );

        long bookingId = bookingService.createBookingWithPayment(
                deskId, phone, start, end, total, mode, paid
        );

        System.out.println("Booking SUCCESS. Booking ID = " + bookingId);
    }
}
