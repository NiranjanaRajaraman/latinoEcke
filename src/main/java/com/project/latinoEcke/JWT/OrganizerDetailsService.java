package com.project.latinoEcke.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.latinoEcke.data.OrganizerRepository;
import com.project.latinoEcke.data.UserRepository;

@Service
public class OrganizerDetailsService implements UserDetailsService{

	@Autowired
	OrganizerRepository organizerRepository;
	
	private com.project.latinoEcke.domain.Organizer organizerDetail;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		organizerDetail= organizerRepository.findByEmail(username);
		if(Objects.nonNull(organizerDetail)) 
			return new User(organizerDetail.getEmail(), organizerDetail.getPassWord(), new ArrayList<>());
		
		else 
			throw new UsernameNotFoundException("User not Found!");
		
	}
	
	
	public com.project.latinoEcke.domain.Organizer getUserDetail(){
//		com.inn.cafe.domain.User  user= userDetail;
//		user.setPassWord(null);
//		return user;
		return organizerDetail;
	} 
	

}


