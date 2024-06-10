package com.project.latinoEcke.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.project.latinoEcke.domain.User;
import com.project.latinoEcke.dto.CustomResponse;

public interface UserService {

	ResponseEntity<String> signUp(Map<String, String> requestMap);

	ResponseEntity<String> login(Map<String, String> requestMap);

	ResponseEntity<String> generateAdminId(Map<String, String> requestMap) throws Exception;

	ResponseEntity<List<User>> getAllAdmins();

	ResponseEntity<CustomResponse<User>> getAdminByAdminId(String adminId);

	ResponseEntity<String> updateByAdminId(Map<String, String> requestMap);

	ResponseEntity<String> deleteByAdminId(Map<String, String> requestMap);

	User getUserProfile();

}
