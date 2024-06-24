package com.project.latinoEcke.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.latinoEcke.JWT.CustomerUserDetailsService;
import com.project.latinoEcke.JWT.JWTFilter;
//import com.project.latinoEcke.JWT.JWTFilter;
import com.project.latinoEcke.JWT.JWTUtil;
import com.project.latinoEcke.data.UserRepository;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.domain.User;
import com.project.latinoEcke.dto.CustomResponse;
import com.project.latinoEcke.service.UserService;
import com.project.latinoEcke.utils.EmailUtils;
import com.project.latinoEcke.utils.ProjectUtils;

@Service
public class UserServiceImpl implements UserService {
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

	private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {

			if (validateSignUpMap(requestMap)) {
				User userObj = userRepository.findByEmailAndAdminId(requestMap.get("email"), requestMap.get("adminId"));
				if (Objects.nonNull(userObj)) {
					userRepository.save(getUserFromMap(requestMap, userObj));
					return ProjectUtils.getResponseEntity("Admin signed Up successfully", HttpStatus.OK);
				} else {
					return ProjectUtils.getResponseEntity("Incorrect email/Incorrect adminID! Contact team",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {

		boolean bool = requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("passWord")
				&& requestMap.containsKey("adminId");
		return bool;
	}

	private User getUserFromMap(Map<String, String> requestMap, User userObj) {

		// user.setName(requestMap.get("name"));
		userObj.setContactNumber(requestMap.get("contactNumber"));
		// user.setEmail(requestMap.get("email"));
		userObj.setPassWord(requestMap.get("passWord"));
		// user.setRole(requestMap.get("role"));
		// user.setIsEnabled(false);
		return userObj;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {

			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("passWord")));
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getIsEnabled().equals(true)) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
											customerUserDetailsService.getUserDetail().getRole(),
											customerUserDetailsService.getUserDetail().getName())
									+ "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<String>("{\"message\":\"" + "Incorrect Details, Please try again" + "\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<String> generateAdminId(Map<String, String> requestMap) {
		try {
			String currentUserRole = jwtFilter.getUserRole();
			if (currentUserRole.equalsIgnoreCase("superAdmin")) {
				String firstName = requestMap.get("firstName").substring(0, 1).toUpperCase()
						+ requestMap.get("firstName").substring(1).toLowerCase();
				String lastName = requestMap.get("lastName").substring(0, 1).toUpperCase()
						+ requestMap.get("lastName").substring(1).toLowerCase();
				String randomCode = UUID.randomUUID().toString().substring(0, 5);
				String token = randomCode.toUpperCase() + "@" + firstName;
				User userObj = new User();
				userObj.setAdminId(token);
				userObj.setRole("admin");
				userObj.setName(firstName + " " + lastName);
				userObj.setEmail(requestMap.get("email"));
				userObj.setIsEnabled(true);
				userRepository.save(userObj);
				sendIdToAdmin(firstName, requestMap.get("email"), token);
				return ProjectUtils.getResponseEntity("Mail sent", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("{\"message\":\"" + "Access Denied." + "\"}", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProjectUtils.getResponseEntity("Mail not sent", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private void sendIdToAdmin(String name, String Useremail, String token) {

		try {
			emailUtils.sendSimpleMessage(name, Useremail, token, "Latino Ecke- Account created", null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public ResponseEntity<List<User>> getAllAdmins() {
		try {
			 if (jwtFilter.isSuperAdmin()) {
			List<User> userFromDb = userRepository.findByRole("admin");
			if (Objects.nonNull(userFromDb) || !userFromDb.isEmpty()) {
				return new ResponseEntity<>(userFromDb, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}

			 } else {
			 return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);

			 }

		} catch (

		Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<CustomResponse<User>> getAdminByAdminId(String adminId) {
		try {
			if (jwtFilter.isSuperAdmin()) {

				User userObjFromDb = userRepository.findByAdminId(adminId);
				if (Objects.nonNull(userObjFromDb)) {
					CustomResponse<User> response = new CustomResponse<>(userObjFromDb, "User found");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					CustomResponse<User> response = new CustomResponse<>(null, "User not found");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}

			} else {
				CustomResponse<User> response = new CustomResponse<>(null, "Access Denied");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		CustomResponse<User> response = new CustomResponse<>(null, "Something went Wrong");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateByAdminId(Map<String, String> requestMap) {
		try {
			if (validateSignUpMap(requestMap)) {
				if (jwtFilter.isSuperAdmin()) {
					User userObj = userRepository.findByAdminId(requestMap.get("adminId"));
					if (Objects.nonNull(userObj)) {
						updateUserFromMap(requestMap, userObj);
						return ProjectUtils.getResponseEntity("Account updated!", HttpStatus.INTERNAL_SERVER_ERROR);

					} else {
						return ProjectUtils.getResponseEntity("Admin ID not found", HttpStatus.BAD_REQUEST);
					}

				} else {
					return ProjectUtils.getResponseEntity("Access Denied", HttpStatus.BAD_REQUEST);
				}
			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ProjectUtils.getResponseEntity("Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void updateUserFromMap(Map<String, String> requestMap, User userObj) {

		userObj.setName(requestMap.get("name"));
		userObj.setContactNumber(requestMap.get("contactNumber"));
		userObj.setEmail(requestMap.get("email"));
		userObj.setPassWord(requestMap.get("passWord"));
		userObj.setRole(requestMap.get("role"));
		userObj.setIsEnabled(true);
		userRepository.save(userObj);
	}

	@Override
	public ResponseEntity<String> deleteByAdminId(Map<String, String> requestMap) {
		try {

			if (jwtFilter.isSuperAdmin()) {
				if (requestMap.containsKey("adminId")) {
					User userObj = userRepository.findByAdminId(requestMap.get("adminId"));
					if (Objects.nonNull(userObj)) {
						userRepository.delete(userObj);
						return ProjectUtils.getResponseEntity("Admin account deleted!", HttpStatus.OK);

					} else {
						return ProjectUtils.getResponseEntity("Admin ID not found", HttpStatus.BAD_REQUEST);
					}

				} else {
					return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}

			} else {
				return ProjectUtils.getResponseEntity("Access Denied", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProjectUtils.getResponseEntity("Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public User getUserProfile() {
		try {

			User userObjFromDb = userRepository.findByEmail(jwtFilter.getCurrentUser());
			if (Objects.nonNull(userObjFromDb)) {

				return userObjFromDb;
			} else {

				return new User();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new User();
	}

}
