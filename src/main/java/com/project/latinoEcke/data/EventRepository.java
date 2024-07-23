package com.project.latinoEcke.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.latinoEcke.domain.Event;

public interface EventRepository extends MongoRepository<Event, String> {

	List<Event> findByIsApproved(boolean isApproved);

//	List<Event> findByEmail(String currentUser);

	Event findByEventId(String eventId);

	List<Event> findByOrganizerId(String email);

	List<Event> findByDate(String date);

	List<Event> findByDateAndIsApproved(String string, boolean b);

}
