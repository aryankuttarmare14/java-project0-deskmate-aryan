package com.deskmate.controller;

import com.deskmate.constants.PaymentMode;
import com.deskmate.service.BookingService;
import com.deskmate.util.DateUtil;
import com.deskmate.util.InputUtil;
import com.deskmate.util.MoneyUtil;

import java.time.LocalDateTime;

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void menu() {

        while (true) {
            System.out.println("\n--- Bookings ---");
            System.out.println("1. Create booking + payment (transaction)");
            System.out.println("0. Back");

            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> createBooking();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createBooking() {

        String deskCode = InputUtil.readString("deskCode: ");
        String phone = InputUtil.readString("customerPhone (10 digits): ");

        LocalDateTime start = DateUtil.parseDateTime(
                InputUtil.readString("slotStart (yyyy-MM-dd HH:mm): ")
        );

        LocalDateTime end = DateUtil.parseDateTime(
                InputUtil.readString("slotEnd (yyyy-MM-dd HH:mm): ")
        );

        var total = MoneyUtil.parse(
                InputUtil.readString("totalAmount: ")
        );

        System.out.println("Payment mode: 1) CASH 2) CARD 3) UPI");
        int pm = InputUtil.readInt("Choose: ");

        PaymentMode mode = switch (pm) {
            case 1 -> PaymentMode.CASH;
            case 2 -> PaymentMode.CARD;
            case 3 -> PaymentMode.UPI;
            default -> throw new IllegalArgumentException("Invalid payment mode");
        };

        var paid = MoneyUtil.parse(
                InputUtil.readString("paidAmount (must match total): ")
        );

        long bookingId = bookingService.createBookingWithPayment(
                deskCode,
                phone,
                start,
                end,
                total,
                mode,
                paid
        );

        System.out.println("Booking created and PAID. bookingId=" + bookingId);
    }
}
