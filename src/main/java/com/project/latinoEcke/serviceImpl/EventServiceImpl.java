package com.project.latinoEcke.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.project.latinoEcke.JWT.CustomerUserDetailsService;
import com.project.latinoEcke.JWT.JWTFilter;
import com.project.latinoEcke.JWT.JWTUtil;
import com.project.latinoEcke.data.EventRepository;
import com.project.latinoEcke.data.OrganizerRepository;
import com.project.latinoEcke.data.UserRepository;
import com.project.latinoEcke.domain.Event;
import com.project.latinoEcke.domain.Organizer;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.service.EventService;
import com.project.latinoEcke.utils.EmailUtils;
import com.project.latinoEcke.utils.ProjectUtils;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	OrganizerRepository organizerRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JWTFilter jwtFilter;

	@Autowired
	EmailUtils emailUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustomerUserDetailsService customerUserDetailsService;

	@Autowired
	JWTUtil jwtUtil;


	@Override
	public ResponseEntity<String> addEvent(Map<String, String> requestMap) {
		try {
			Organizer organizerObj = organizerRepository.findByEmail(jwtFilter.getCurrentUser());
			if (Objects.nonNull(organizerObj)) {
				if (validateMap(requestMap)) {
					Event event = new Event();
//					String base64Image = ImageUtils.encodeMultipartFileToBase64(image);
//		            event.setEventId(requestMap.get("EventId"));
//		            event.setImage(base64Image);
					
					event.setTitle(requestMap.get("title"));
					event.setDescription(requestMap.get("description"));
					event.setCategory(requestMap.get("category"));
					event.setAddressLine(requestMap.get("addressLine"));
					event.setDate(requestMap.get("date")); // assuming dateTime is provided as
					event.setKeyWords(requestMap.get("category")+" "+ requestMap.get("title")+" "+requestMap.get("zipCode")+ " "+ requestMap.get("city"));														// timestamp
					event.setTime(requestMap.get("time"));
					event.setCity(requestMap.get("city"));
					event.setZipCode(requestMap.get("zipCode"));
					event.setIsApproved(false);
					event.setPrice(requestMap.get("price"));
					event.setOrganizerId(organizerObj.getOrganizerId());
					String randomCode = UUID.randomUUID().toString().substring(0, 6);
					String token = randomCode.toUpperCase();
					event.setEventId(token);
					eventRepository.save(event);

					return ProjectUtils.getResponseEntity("Event Added successfully!", HttpStatus.OK);
				} else {
					return ProjectUtils.getResponseEntity("Incorrect Details", HttpStatus.BAD_REQUEST);
				}

			} else {
				return ProjectUtils.getResponseEntity("Your Email not found to add events", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private boolean validateMap(Map<String, String> requestMap) {

		boolean bool = requestMap.containsKey("title") && requestMap.containsKey("description")
				&& requestMap.containsKey("category") && requestMap.containsKey("addressLine") && requestMap.containsKey("city") && requestMap.containsKey("zipCode")
				&& requestMap.containsKey("date") && requestMap.containsKey("price") && requestMap.containsKey("time") ;
		return bool;
	}

	
	@Override
	public ResponseEntity<List<Event>> getAllApprovedEvents() {
		try {
			if (jwtFilter.isSuperAdmin() || jwtFilter.isAdmin()) {
				List<Event> userFromDb = eventRepository.findByIsApproved(true);
				if (Objects.nonNull(userFromDb) || !userFromDb.isEmpty()) {
					return new ResponseEntity<>(userFromDb, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
				}

			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Event>> getEventsToApprove() {
		try {
			if (jwtFilter.isSuperAdmin() || jwtFilter.isAdmin()) {
				List<Event> userFromDb = eventRepository.findByIsApproved(false);
				if (Objects.nonNull(userFromDb) || !userFromDb.isEmpty()) {
					return new ResponseEntity<>(userFromDb, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
				}

			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> approveEvent(Map<String, String> requestMap) {
		try {
			if (validateApproveMap(requestMap)) {
				Event userObj = eventRepository.findByEventId(requestMap.get("eventId"));
				if (Objects.nonNull(userObj)) {
					userObj.setIsApproved(true);
					userObj.setApprovedBy(jwtFilter.getCurrentUser());
					eventRepository.save(userObj);
					return ProjectUtils.getResponseEntity("Approved successfully", HttpStatus.OK);

				} else {
					return ProjectUtils.getResponseEntity("EventId not found", HttpStatus.BAD_REQUEST);
				}

			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<>(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateApproveMap(Map<String, String> requestMap) {
		boolean bool = requestMap.containsKey("eventId");
		return bool;
	}

	@Override
	public ResponseEntity<List<Event>> getEventsByCurrentUser() {
		try {
				Organizer userFromDb = organizerRepository.findByEmail(jwtFilter.getCurrentUser());
				if (Objects.nonNull(userFromDb)) {
					List<Event> events = eventRepository.findByOrganizerId(userFromDb.getOrganizerId()); 	
					return new ResponseEntity<>(events, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateEvent(Map<String, String> requestMap) {
		try {
			Organizer organizerObj = organizerRepository.findByEmail(jwtFilter.getCurrentUser());
			if (Objects.nonNull(organizerObj)) {
				if (validateUpdateMap(requestMap)) {
					Event event = eventRepository.findByEventId(requestMap.get("eventId"));
//					String base64Image = ImageUtils.encodeMultipartFileToBase64(image);
//		            event.setEventId(requestMap.get("EventId"));
//		            event.setImage(base64Image);
					if (Objects.nonNull(event)) {
						
						event.setTitle(requestMap.get("title"));
						event.setDescription(requestMap.get("description"));
						event.setCategory(requestMap.get("category"));
						event.setAddressLine(requestMap.get("addressLine"));
						event.setDate(requestMap.get("date")); // assuming dateTime is provided as
						event.setKeyWords(requestMap.get("category")+" "+ requestMap.get("title")+" "+requestMap.get("zipCode")+" "+ requestMap.get("city"));														// timestamp
						event.setTime(requestMap.get("time"));
						event.setCity(requestMap.get("city"));
						event.setZipCode(requestMap.get("zipCode"));
						event.setPrice(requestMap.get("price"));
						event.setOrganizerId(organizerObj.getOrganizerId());
						eventRepository.save(event);

						return ProjectUtils.getResponseEntity("Event Updated successfully!", HttpStatus.OK);
					}
					else {
						return ProjectUtils.getResponseEntity("EventId not found!", HttpStatus.BAD_REQUEST);
					}
					
				} else {
					return ProjectUtils.getResponseEntity("Incorrect Details", HttpStatus.BAD_REQUEST);
				}

			} else {
				return ProjectUtils.getResponseEntity("Your Email not found to add events", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private boolean validateUpdateMap(Map<String, String> requestMap) {

		boolean bool = requestMap.containsKey("title") && requestMap.containsKey("description") && requestMap.containsKey("eventId")
				&& requestMap.containsKey("category") && requestMap.containsKey("addressLine") && requestMap.containsKey("city") && requestMap.containsKey("zipCode")
				&& requestMap.containsKey("date") && requestMap.containsKey("price") && requestMap.containsKey("time") ;
		return bool;
	}

	@Override
	public ResponseEntity<List<Event>> getAllEvents() {
		try {
			
				List<Event> userFromDb = eventRepository.findByIsApproved(true);
				if (Objects.nonNull(userFromDb) || !userFromDb.isEmpty()) {
					return new ResponseEntity<>(userFromDb, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
				}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Event>> getEventsByDate(String Date) {
		try {
			
			List<Event> userFromDb = eventRepository.findByDate(Date);
			if (Objects.nonNull(userFromDb) || !userFromDb.isEmpty()) {
				return new ResponseEntity<>(userFromDb, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}


	} catch (Exception ex) {
		ex.printStackTrace();
	}
	return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Map<String, List<Event>>> GetUpcomingEvents() {
   try {
			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);
			LocalDate dayAfterTomorrow = today.plusDays(2);
			
			HashMap<String, List<Event>> mapResponse = new HashMap<>();
			
			List<Event> todayEvents = eventRepository.findByDateAndIsApproved(today.toString(),true);
			List<Event> tomorrowEvents = eventRepository.findByDateAndIsApproved(tomorrow.toString(),true);
			List<Event> dayAfterTomorrowEvents = eventRepository.findByDateAndIsApproved(dayAfterTomorrow.toString(),true);
			if(!todayEvents.isEmpty()) {
				mapResponse.put("today", todayEvents);
			}
			else {
				mapResponse.put("today", new ArrayList<>());
			}
			if(!tomorrowEvents.isEmpty()) {
				mapResponse.put("tomorrow", tomorrowEvents);
			}else {
				mapResponse.put("tomorrow", new ArrayList<>());
			}
			if(!dayAfterTomorrowEvents.isEmpty()) {
				mapResponse.put("dayAfterTomorrow", dayAfterTomorrowEvents);
			}else {
				mapResponse.put("dayAfterTomorrow", new ArrayList<>());
			}
				return new ResponseEntity<>(mapResponse, HttpStatus.OK);
			

	} catch (Exception ex) {
		ex.printStackTrace();
	}
	return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
