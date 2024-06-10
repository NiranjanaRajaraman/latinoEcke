package com.project.latinoEcke.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.domain.User;
import com.project.latinoEcke.dto.CustomResponse;
import com.project.latinoEcke.service.UserService;
import com.project.latinoEcke.utils.ProjectUtils;

@RestController
@RequestMapping(path="/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping(path= "/signUp")
	public ResponseEntity<String> SignUp(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.signUp(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping(path= "/login")
	public ResponseEntity<String> login(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.login(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping(path= "/generate-adminId")
	public ResponseEntity<String> generateAdminId(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.generateAdminId(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@GetMapping(path= "/getAllAdmins")
	public ResponseEntity<List<User>> getAllAdmins(){
		try {
			return userService.getAllAdmins();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<User>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/getAdminByAdminId")
	public ResponseEntity<CustomResponse<User>> getAdminByAdminId(@RequestParam(required=true) String adminId){
		try {
			return userService.getAdminByAdminId(adminId);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		CustomResponse<User> response = new CustomResponse<>(null, "Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@GetMapping(path= "/getUserProfile")
	public User getUserProfile(){
		try {
			return userService.getUserProfile();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		
        return new User();
	}
	
	@PostMapping(path= "/updateByAdminId")
	public ResponseEntity<String> updateByAdminId(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.updateByAdminId(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@DeleteMapping(path= "/deleteByAdminId")
	public ResponseEntity<String> deleteByAdminId(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.deleteByAdminId(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
