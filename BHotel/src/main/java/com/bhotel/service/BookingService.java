package com.bhotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhotel.exception.InvalidBookingRequestException;
import com.bhotel.exception.ResourceNotFoundException;
import com.bhotel.model.BookedRoom;
import com.bhotel.model.Room;
import com.bhotel.repository.BookingRepository;
import com.bhotel.service.Interfaces.IBookingService;
import com.bhotel.service.Interfaces.IRoomService;

@Service
public class BookingService implements IBookingService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private IRoomService roomService;

	@Override
	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(
				() -> new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));

	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must come before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	// Valid Check in and Check out dates
	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream().noneMatch(existingBooking -> {
			// Extract dates for better readability
			var newCheckIn = bookingRequest.getCheckInDate();
			var newCheckOut = bookingRequest.getCheckOutDate();
			var existingCheckIn = existingBooking.getCheckInDate();
			var existingCheckOut = existingBooking.getCheckOutDate();

			// Check if the requested booking overlaps with any existing booking
			return (newCheckIn.equals(existingCheckIn) || newCheckOut.equals(existingCheckOut)) // Same day overlaps
					|| (newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn)) // Overlapping
																										// dates
					|| (newCheckIn.equals(existingCheckOut) && newCheckOut.equals(existingCheckIn)); // Inverse overlap
		});
	}

//    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
//        return existingBookings.stream()
//                .noneMatch(existingBooking ->
//                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
//                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
//                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
//                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
//                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//
//                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
//                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//
//                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
//
//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
//
//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
//                );
//    }
//    
}
