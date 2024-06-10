package com.project.latinoEcke.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("Organizer")
public class Organizer {
	
	@Id
	private String _id;

	private String organizerId;
	private String organizerName;
	private String contactNumber;
	private String email;
	private String passWord;
	private List<Event> eventHosted;
	
	public String getOrganizerId() {
		return organizerId;
	}
	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}
	public String getOrganizerName() {
		return organizerName;
	}
	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public List<Event> getEventHosted() {
		return eventHosted;
	}
	public void setEventHosted(List<Event> eventHosted) {
		this.eventHosted = eventHosted;
	}
}
