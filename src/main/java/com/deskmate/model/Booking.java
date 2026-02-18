package com.deskmate.model;

import com.deskmate.constants.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {

    private long bookingId;
    private long deskId;
    private String phone;
    private LocalDateTime start;
    private LocalDateTime end;
    private BigDecimal totalAmount;
    private BookingStatus status;

    public Booking(long bookingId, long deskId, String phone,
                   LocalDateTime start, LocalDateTime end,
                   BigDecimal totalAmount, BookingStatus status) {
        this.bookingId = bookingId;
        this.deskId = deskId;
        this.phone = phone;
        this.start = start;
        this.end = end;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public long getBookingId() {
        return bookingId;
    }
}