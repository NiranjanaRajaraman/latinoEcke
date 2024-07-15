package com.project.latinoEcke.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.project.latinoEcke.domain.Organizer;

public interface OrganizerService {

	ResponseEntity<String> signUp(Map<String, String> requestMap);

	ResponseEntity<String> login(Map<String, String> requestMap);

	ResponseEntity<List<Organizer>> getOrganizersToApprove();

	ResponseEntity<String> approveOrganizer(Map<String, String> requestMap);

	ResponseEntity<List<Organizer>> getApprovedOrganizers();

	ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

	ResponseEntity<String> resetPassword(Map<String, String> requestMap);

}
