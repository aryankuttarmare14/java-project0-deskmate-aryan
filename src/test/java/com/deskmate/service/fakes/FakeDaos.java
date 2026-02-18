package com.deskmate.service.fakes;

import com.deskmate.constants.BookingStatus;
import com.deskmate.dao.*;
import com.deskmate.model.Desk;
import com.deskmate.model.Payment;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeDaos {
	public static class FakeDeskDao implements DeskDao {
		private final Map<String, Desk> byCode = new HashMap<>();
		private final AtomicLong ids = new AtomicLong(1);

		public FakeDeskDao seedDesk(String code, boolean active) {
			byCode.put(code, new Desk(ids.getAndIncrement(), code, code, active, LocalDateTime.now()));
			return this;
		}

		@Override
		public long insertDesk(String code, String name) {
			return 1;
		}

		@Override
		public void deactivateDesk(long deskId) {
		}

		@Override
		public Optional<Desk> findByCode(String code) {
			return Optional.ofNullable(byCode.get(code));
		}

		@Override
		public Optional<Desk> findById(long deskId) {
			return Optional.empty();
		}

		@Override
		public List<Desk> listActive() {
			return byCode.values().stream().filter(Desk::isActive).toList();
		}
	}

	public static class FakeBookingDao implements BookingDao {
		private final Set<String> unique = new HashSet<>();
		private final AtomicLong ids = new AtomicLong(10);
		private BookingStatus lastStatus;

		@Override
		public long insertBooking(Connection conn, long deskId, String phone, LocalDateTime start, LocalDateTime end,
				BigDecimal total, BookingStatus status) {
			String key = deskId + "|" + start.toString();
			if (!unique.add(key))
				throw new RuntimeException("UNIQUE_VIOLATION");
			lastStatus = status;
			return ids.getAndIncrement();
		}

		@Override
		public void updateStatus(Connection conn, long bookingId, BookingStatus status) {
			lastStatus = status;
		}

		@Override
		public Optional<com.deskmate.model.Booking> findById(long bookingId) {
			return Optional.empty();
		}

		public BookingStatus lastStatus() {
			return lastStatus;
		}
	}

	public static class FakePaymentDao implements PaymentDao {
		@Override
		public long insertPayment(Connection conn, Payment payment) {
			return 1;
		}

		@Override
		public Optional<Payment> findByBookingId(long bookingId) {
			return Optional.empty();
		}
	}
}
