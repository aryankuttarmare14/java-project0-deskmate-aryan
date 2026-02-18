package com.deskmate.service;

import com.deskmate.constants.BookingStatus;
import com.deskmate.constants.PaymentMode;
import com.deskmate.constants.PaymentStatus;
import com.deskmate.dao.BookingDao;
import com.deskmate.dao.DeskDao;
import com.deskmate.dao.PaymentDao;
import com.deskmate.exception.*;
import com.deskmate.model.Desk;
import com.deskmate.model.Payment;
import com.deskmate.util.DbConnectionFactory;
import com.deskmate.util.ValidationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

public class BookingService {

    private static final Logger log =
            LogManager.getLogger(BookingService.class);

    private final DeskDao deskDao;
    private final BookingDao bookingDao;
    private final PaymentDao paymentDao;

    public BookingService(DeskDao deskDao,
                          BookingDao bookingDao,
                          PaymentDao paymentDao) {
        this.deskDao = deskDao;
        this.bookingDao = bookingDao;
        this.paymentDao = paymentDao;
    }

    public long createBookingWithPayment(
            String deskCode,
            String customerPhone,
            LocalDateTime slotStart,
            LocalDateTime slotEnd,
            BigDecimal totalAmount,
            PaymentMode mode,
            BigDecimal paidAmount
    ) {

        // 1) Validate early
        ValidationUtil.requireNonBlank(deskCode, "deskCode");

        String phone = ValidationUtil.normalizePhone(customerPhone);

        if (slotStart == null || slotEnd == null ||
                !slotEnd.isAfter(slotStart)) {
            throw new ValidationException(
                    "Invalid slot times: slotEnd must be after slotStart");
        }

        if (totalAmount == null || totalAmount.signum() < 0) {
            throw new ValidationException("totalAmount must be >= 0");
        }

        if (paidAmount == null || paidAmount.signum() < 0) {
            throw new ValidationException("paidAmount must be >= 0");
        }

        Desk desk = deskDao.findByCode(deskCode)
                .orElseThrow(() ->
                        new EntityNotFoundException("Desk not found: " + deskCode));

        if (!desk.isActive()) {
            throw new ValidationException("Desk is inactive: " + deskCode);
        }

        // Payment mismatch rule (v1)
        if (paidAmount.compareTo(totalAmount) != 0) {
            throw new ValidationException(
                    "Payment amount mismatch. Expected " +
                            totalAmount + " but got " + paidAmount);
        }

        // 2) Transaction boundary
        try (Connection conn = DbConnectionFactory.getConnection()) {

            conn.setAutoCommit(false);

            long bookingId;

            try {

                bookingId = bookingDao.insertBooking(
                        conn,
                        desk.getDeskId(),
                        phone,
                        slotStart,
                        slotEnd,
                        totalAmount,
                        BookingStatus.CREATED
                );

                Payment payment = new Payment(
                        0,
                        bookingId,
                        mode,
                        paidAmount,
                        PaymentStatus.SUCCESS,
                        LocalDateTime.now()
                );

                paymentDao.insertPayment(conn, payment);

                bookingDao.updateStatus(
                        conn,
                        bookingId,
                        BookingStatus.PAID
                );

                conn.commit();

                log.info("Booking PAID: bookingId={}", bookingId);

                return bookingId;

            } catch (DatabaseOperationException ex) {
                // Possible double booking or constraint violation
                rollbackQuietly(conn);
                throw ex;

            } catch (Exception ex) {

                rollbackQuietly(conn);

                throw new DatabaseOperationException(
                        "Checkout failed and was rolled back", ex);
            }

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new DatabaseOperationException(
                    "DB failure during checkout", e);
        }
    }

    private void rollbackQuietly(Connection connection) {
        try {
            connection.rollback();
        } catch (Exception ignore) {
        }
    }
}
