package com.project.latinoEcke.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.latinoEcke.domain.Organizer;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.service.OrganizerService;
import com.project.latinoEcke.utils.ProjectUtils;

@RestController
@RequestMapping(path = "/user")
public class OrganizerController {
	@Autowired
	OrganizerService organizerService;

	@PostMapping(path = "/organizer/signUp")
	public ResponseEntity<String> SignUp(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return organizerService.signUp(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/organizer/getOrganizers-to-Approve")
	public ResponseEntity<List<Organizer>> getOrganizersToApprove() {
		try {
			return organizerService.getOrganizersToApprove();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return new ResponseEntity<List<Organizer>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/organizer/get-approvedOrganizers")
	public ResponseEntity<List<Organizer>> getApprovedOrganizers() {
		try {
			return organizerService.getApprovedOrganizers();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return new ResponseEntity<List<Organizer>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/organizer/approve-organizer")
	public ResponseEntity<String> approveOrganizer(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return organizerService.approveOrganizer(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path = "/organizer/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return organizerService.forgotPassword(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping(path = "/organizer/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return organizerService.resetPassword(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping(path= "/organizer/login")
	public ResponseEntity<String> login(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return organizerService.login(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
