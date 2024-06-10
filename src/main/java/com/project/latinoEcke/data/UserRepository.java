package com.project.latinoEcke.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.latinoEcke.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByEmail(String requestEmailId);

	User findByAdminId(String token);

	User findByEmailAndAdminId(String email, String adminId);

	List<User> findByRole(String role);

}