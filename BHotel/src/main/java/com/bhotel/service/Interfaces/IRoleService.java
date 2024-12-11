package com.bhotel.service.Interfaces;

import java.util.List;

import com.bhotel.model.Role;
import com.bhotel.model.User;

public interface IRoleService {

	List<Role> getRoles();

	Role createRole(Role theRole);

	void deleteRole(Long id);

	Role findByName(String name);

	User removeUserFromRole(Long id, Long roleId);

	User assignRoleToUser(Long id, Long roleId);

	Role removeAllUsersFromRole(Long roleId);
}
