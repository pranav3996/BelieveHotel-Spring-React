package com.bhotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhotel.exception.ResourceNotFoundException;
import com.bhotel.model.BookedRoom;
import com.bhotel.repository.BookingRepository;
import com.bhotel.service.Interfaces.IBookingService;
import com.bhotel.service.Interfaces.IRoomService;

@Service
public class BookingService implements IBookingService {

	@Autowired
    private  BookingRepository bookingRepository;
	@Autowired
    private  IRoomService roomService;
	

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
    
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }
}
