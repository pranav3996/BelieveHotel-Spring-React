package com.bhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhotel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
