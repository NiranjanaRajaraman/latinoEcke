package com.project.latinoEcke.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.latinoEcke.data.UserRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	private com.project.latinoEcke.domain.User userDetail;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		userDetail= userRepository.findByEmail(username);
		if(Objects.nonNull(userDetail)) 
			return new User(userDetail.getEmail(), userDetail.getPassWord(), new ArrayList<>());
		
		else 
			throw new UsernameNotFoundException("User not Found!");
		
	}
	
	
	public com.project.latinoEcke.domain.User getUserDetail(){
//		com.inn.cafe.domain.User  user= userDetail;
//		user.setPassWord(null);
//		return user;
		return userDetail;
	} 
	

}

