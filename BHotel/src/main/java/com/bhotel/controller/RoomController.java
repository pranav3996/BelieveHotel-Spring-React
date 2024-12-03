package com.bhotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bhotel.model.Room;
import com.bhotel.response.RoomResponse;
import com.bhotel.service.BookingService;
import com.bhotel.service.Interfaces.IRoomService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
    private  IRoomService roomService;
    private final BookingService bookingService = new BookingService();
	
	   @PostMapping("/add/new-room")
//	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public ResponseEntity<RoomResponse> addNewRoom(
	            @RequestParam("photo") MultipartFile photo,
	            @RequestParam("roomType") String roomType,
	            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
	      
		   Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
	        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
	                savedRoom.getRoomPrice());
	        return ResponseEntity.ok(response);
	    }
}