package com.bhotel.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bhotel.model.Room;
import com.bhotel.repository.RoomRepository;
import com.bhotel.service.Interfaces.IRoomService;

import javax.sql.rowset.serial.SerialBlob;
import lombok.RequiredArgsConstructor;
import java.sql.Blob;

@Service
public class RoomService implements IRoomService {
	
	@Autowired
    private  RoomRepository roomRepository ;
    
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }
	@Override
	public List<String> getAllRoomTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Room> getAllRooms() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteRoom(Long roomId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional<Room> getRoomById(Long roomId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	@Override
	public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
		// TODO Auto-generated method stub
		return null;
	}

}
