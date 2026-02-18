package com.deskmate.service;

import com.deskmate.constants.BookingStatus;
import com.deskmate.constants.PaymentMode;
import com.deskmate.constants.PaymentStatus;
import com.deskmate.utils.DbConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class BookingService {

    public long createBookingWithPayment(
            long deskId,
            String phone,
            LocalDateTime start,
            LocalDateTime end,
            BigDecimal totalAmount,
            PaymentMode mode,
            BigDecimal paidAmount
    ) {

        if (!end.isAfter(start)) {
            throw new RuntimeException("Slot end must be after start");
        }

        if (paidAmount.compareTo(totalAmount) != 0) {
            throw new RuntimeException("Payment amount mismatch");
        }

        try (Connection conn = DbConnectionFactory.getConnection()) {

            conn.setAutoCommit(false);

            long bookingId;

            try {

                // Insert booking
                String bookingSql = "INSERT INTO bookings " +
                        "(desk_id, customer_phone, slot_start, slot_end, total_amount, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, deskId);
                    ps.setString(2, phone);
                    ps.setTimestamp(3, Timestamp.valueOf(start));
                    ps.setTimestamp(4, Timestamp.valueOf(end));
                    ps.setBigDecimal(5, totalAmount);
                    ps.setString(6, BookingStatus.CREATED.name());

                    ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    bookingId = rs.getLong(1);
                }

                // Insert payment
                String paymentSql = "INSERT INTO payments " +
                        "(booking_id, mode, amount, status, paid_at) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(paymentSql)) {
                    ps.setLong(1, bookingId);
                    ps.setString(2, mode.name());
                    ps.setBigDecimal(3, paidAmount);
                    ps.setString(4, PaymentStatus.SUCCESS.name());
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

                    ps.executeUpdate();
                }

                // Update booking to PAID
                String updateSql = "UPDATE bookings SET status=? WHERE booking_id=?";

                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setString(1, BookingStatus.PAID.name());
                    ps.setLong(2, bookingId);
                    ps.executeUpdate();
                }

                conn.commit();
                return bookingId;

            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Transaction failed. Rolled back.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}
