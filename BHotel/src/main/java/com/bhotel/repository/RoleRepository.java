package com.bhotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhotel.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	
	Optional<Role> findByName(String role);

}
