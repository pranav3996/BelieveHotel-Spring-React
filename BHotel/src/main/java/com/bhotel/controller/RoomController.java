package com.bhotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bhotel.exception.PhotoRetrievalException;
import com.bhotel.model.BookedRoom;
import com.bhotel.model.Room;
import com.bhotel.response.BookingResponse;
import com.bhotel.response.RoomResponse;
import com.bhotel.service.BookingService;
import com.bhotel.service.Interfaces.IRoomService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/rooms")
@CrossOrigin
public class RoomController {

	@Autowired
    private  IRoomService roomService;
	
	@Autowired
    private  BookingService bookingService ;
	
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
	   
	   @GetMapping("/room/types")
	    public List<String> getRoomTypes() {
	        return roomService.getAllRoomTypes();
	    }
	   
	   @GetMapping("/all-rooms")
	    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
	        List<Room> rooms = roomService.getAllRooms();
	        List<RoomResponse> roomResponses = new ArrayList<>();
	        for (Room room : rooms) {
	            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
	            if (photoBytes != null && photoBytes.length > 0) {
	                String base64Photo = Base64.encodeBase64String(photoBytes);
	                RoomResponse roomResponse = getRoomResponse(room);
	                roomResponse.setPhoto(base64Photo);
	                roomResponses.add(roomResponse);
	            }
	        }
	        return ResponseEntity.ok(roomResponses);
	    }
	   
	    @DeleteMapping("/delete/room/{roomId}")
//	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
	        roomService.deleteRoom(roomId);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	   
	   
	   private RoomResponse getRoomResponse(Room room) {
	        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
	        
	        /*Booking History*/
	       List<BookingResponse> bookingInfo = bookings
	                .stream()
	                .map(booking -> new BookingResponse(booking.getBookingId(),
	                        booking.getCheckInDate(),
	                        booking.getCheckOutDate(), booking.getBookingConfirmationCode()))
	                .toList();
	        byte[] photoBytes = null; 
	        Blob photoBlob = room.getPhoto();
	        if (photoBlob != null) {
	            try {
	                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
	            } catch (SQLException e) {
	                throw new PhotoRetrievalException("Error retrieving photo");
	            }
	        }
	        return new RoomResponse(room.getId(),
	                room.getRoomType(), room.getRoomPrice(),
	                room.isBooked(), photoBytes, bookingInfo);
	    }

	    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
	        return bookingService.getAllBookingsByRoomId(roomId);

	    }

}
