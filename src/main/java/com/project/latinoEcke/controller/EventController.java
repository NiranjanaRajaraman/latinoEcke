package com.project.latinoEcke.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.latinoEcke.domain.Event;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.service.EventService;
import com.project.latinoEcke.utils.ProjectUtils;

@RestController
@RequestMapping(path="/user")
public class EventController {
	
	@Autowired
	EventService eventService;
	
	@PostMapping(path= "/event/add-event")
	public ResponseEntity<String> AddEvent(@RequestBody Map<String,String> requestMap){
		try {
			return eventService.addEvent(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping(path= "/event/update-event")
	public ResponseEntity<String> UpdateEvent(@RequestBody Map<String,String> requestMap){
		try {
			return eventService.updateEvent(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-allApprovedEvents")
	public ResponseEntity<List<Event>> GetAllApprovedEvents(){
		try {
			return eventService.getAllApprovedEvents();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<Event>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-eventsToApprove")
	public ResponseEntity<List<Event>> GetEventsToApprove(){
		try {
			return eventService.getEventsToApprove();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<Event>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-allEvents")
	public ResponseEntity<List<Event>> GetAllEvents(){
		try {
			return eventService.getAllEvents();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<Event>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-eventsByDate")
	public ResponseEntity<List<Event>> GetEventsByDate(@RequestParam String date){
		try {
			return eventService.getEventsByDate(date);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<Event>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-eventsByCurrentUser")
	public ResponseEntity<List<Event>> GetEventsByCurrentUser(){
		try {
			return eventService.getEventsByCurrentUser();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<Event>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@PostMapping(path = "/event/approve-event")
	public ResponseEntity<String> ApproveEvent(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return eventService.approveEvent(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/event/get-upcomingEvents")
	public ResponseEntity<Map<String, List<Event>>> GetUpcomingEvents(){
		try {
			return eventService.GetUpcomingEvents();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<Map<String, List<Event>>>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
