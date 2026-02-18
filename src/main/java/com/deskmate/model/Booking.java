package com.deskmate.model;

import com.deskmate.constants.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
	private long bookingId;
	private long deskId;
	private String customerPhone;
	private LocalDateTime slotStart;
	private LocalDateTime slotEnd;
	private BigDecimal totalAmount;
	private BookingStatus status;

	public Booking(long bookingId, long deskId, String customerPhone, LocalDateTime slotStart, LocalDateTime slotEnd,
			BigDecimal totalAmount, BookingStatus status) {
		this.bookingId = bookingId;
		this.deskId = deskId;
		this.customerPhone = customerPhone;
		this.slotStart = slotStart;
		this.slotEnd = slotEnd;
		this.totalAmount = totalAmount;
		this.status = status;
	}

	public long getBookingId() {
		return bookingId;
	}

	public long getDeskId() {
		return deskId;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public LocalDateTime getSlotStart() {
		return slotStart;
	}

	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public BookingStatus getStatus() {
		return status;
	}
}