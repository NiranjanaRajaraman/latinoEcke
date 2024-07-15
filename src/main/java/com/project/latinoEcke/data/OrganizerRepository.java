package com.project.latinoEcke.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.latinoEcke.domain.Organizer;

public interface OrganizerRepository extends MongoRepository<Organizer, String> {

	Organizer findByEmail(String email);

	List<Organizer> findByIsApproved(boolean isApproved);

}
