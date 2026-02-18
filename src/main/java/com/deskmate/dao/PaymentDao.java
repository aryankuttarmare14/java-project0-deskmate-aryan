package com.deskmate.dao;

import com.deskmate.model.Payment;
import java.sql.Connection;
import java.util.Optional;

public interface PaymentDao {

    long insertPayment(Connection conn, Payment payment);

    Optional<Payment> findByBookingId(long bookingId);
}
