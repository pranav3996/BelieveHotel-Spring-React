package com.bhotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhotel.exception.ResourceNotFoundException;
import com.bhotel.model.BookedRoom;
import com.bhotel.model.Room;
import com.bhotel.response.BookingResponse;
import com.bhotel.response.RoomResponse;
import com.bhotel.service.Interfaces.IBookingService;
import com.bhotel.service.Interfaces.IRoomService;

@RestController
@RequestMapping("/bookings")
@CrossOrigin
public class BookingController {

	@Autowired
	 private  IBookingService bookingService;
	
	@Autowired   
	private  IRoomService roomService;

	    @GetMapping("/all-bookings")
//	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public ResponseEntity<List<BookingResponse>> getAllBookings(){
	    	
	        List<BookedRoom> bookings = bookingService.getAllBookings();
	        List<BookingResponse> bookingResponses = new ArrayList<>();
	        for (BookedRoom booking : bookings){
	            BookingResponse bookingResponse = getBookingResponse(booking);
	            bookingResponses.add(bookingResponse);
	        }
	        return ResponseEntity.ok(bookingResponses);
	        
	    }
	
	    @GetMapping("/confirmation/{confirmationCode}")
	    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
	        try{
	            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
	            BookingResponse bookingResponse = getBookingResponse(booking);
	            return ResponseEntity.ok(bookingResponse);
	        }catch (ResourceNotFoundException ex){
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	        }
	    }
	    
	    
	    private BookingResponse getBookingResponse(BookedRoom booking) {
	        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
	        RoomResponse room = new RoomResponse(
	                theRoom.getId(),
	                theRoom.getRoomType(),
	                theRoom.getRoomPrice());
	        return new BookingResponse(
	                booking.getBookingId(), booking.getCheckInDate(),
	                booking.getCheckOutDate(),booking.getGuestFullName(),
	                booking.getGuestEmail(), booking.getNumOfAdults(),
	                booking.getNumOfChildren(), booking.getTotalNumOfGuest(),
	                booking.getBookingConfirmationCode(), room);
	    }
}
