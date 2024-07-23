package com.project.latinoEcke.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.project.latinoEcke.domain.Event;

public interface EventService {

	ResponseEntity<String> addEvent(Map<String, String> requestMap);

	ResponseEntity<List<Event>> getAllApprovedEvents();

	ResponseEntity<List<Event>> getEventsToApprove();

	ResponseEntity<String> approveEvent(Map<String, String> requestMap);

	ResponseEntity<List<Event>> getEventsByCurrentUser();

	ResponseEntity<String> updateEvent(Map<String, String> requestMap);

	ResponseEntity<List<Event>> getAllEvents();

	ResponseEntity<List<Event>> getEventsByDate(String date);

	ResponseEntity<Map<String, List<Event>>> GetUpcomingEvents();
}
