package com.bhotel.service.Interfaces;

import java.util.List;

import com.bhotel.model.BookedRoom;

public interface IBookingService {

	List<BookedRoom> getAllBookingsByRoomId(Long roomId);

	List<BookedRoom> getAllBookings();

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	void cancelBooking(Long bookingId);
}
