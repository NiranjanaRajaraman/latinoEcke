package com.project.latinoEcke.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.latinoEcke.JWT.CustomerUserDetailsService;
import com.project.latinoEcke.JWT.JWTFilter;
import com.project.latinoEcke.JWT.JWTUtil;
import com.project.latinoEcke.data.OrganizerRepository;
import com.project.latinoEcke.data.OtpRepository;
import com.project.latinoEcke.domain.Organizer;
import com.project.latinoEcke.domain.Otp;
import com.project.latinoEcke.domain.ProjectConstants;
import com.project.latinoEcke.service.OrganizerService;
import com.project.latinoEcke.utils.EmailUtils;
import com.project.latinoEcke.utils.ProjectUtils;

@Service
public class OrganizerServiceImpl implements OrganizerService {
	@Autowired
	OrganizerRepository organizerRepository;

	@Autowired
	OtpRepository otpRepository;

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

	private static final int OTP_VALID_DURATION = 10;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {

			if (validateSignUpMap(requestMap)) {
				Organizer userObj = organizerRepository.findByEmail(requestMap.get("email"));
				if (Objects.isNull(userObj)) {
					organizerRepository.save(getUserFromMap(requestMap));
					return ProjectUtils.getResponseEntity("Signed Up successfully", HttpStatus.OK);
				} else {
					return ProjectUtils.getResponseEntity("Email Already Exists! Contact team", HttpStatus.BAD_REQUEST);
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

		boolean bool = requestMap.containsKey("organizerName") && requestMap.containsKey("organizationName")
				&& requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
				&& requestMap.containsKey("passWord") && requestMap.containsKey("isPermitted");
		return bool;
	}

	private Organizer getUserFromMap(Map<String, String> requestMap) {
		Organizer userObj = new Organizer();
		userObj.setContactNumber(requestMap.get("contactNumber"));
		userObj.setEmail(requestMap.get("email"));
		userObj.setIsPermitted(Boolean.valueOf(requestMap.get("isPermitted")));
		userObj.setPassWord(requestMap.get("passWord"));
		userObj.setIsApproved(false);
		userObj.setOrganizationName(requestMap.get("organizationName"));
		userObj.setOrganizerName(requestMap.get("organizerName"));

		return userObj;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {

			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("passWord")));
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getOrganizerDetail().getIsApproved().equals(true)) {
					return new ResponseEntity<String>(
							"{\"token\":\"" + jwtUtil.generateOrganizerToken(
									customerUserDetailsService.getOrganizerDetail().getEmail(),
									customerUserDetailsService.getOrganizerDetail().getOrganizationName(),
									customerUserDetailsService.getOrganizerDetail().getOrganizerName()) + "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<String>("{\"message\":\"" + "Incorrect Details, Please try again" + "\"}",
				HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<Organizer>> getOrganizersToApprove() {
		try {
			if (jwtFilter.isSuperAdmin() || jwtFilter.isAdmin()) {
				List<Organizer> userFromDb = organizerRepository.findByIsApproved(false);
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
	public ResponseEntity<String> approveOrganizer(Map<String, String> requestMap) {
		try {
			if (validateApproveMap(requestMap)) {
				Organizer userObj = organizerRepository.findByEmail(requestMap.get("email"));
				if (Objects.nonNull(userObj)) {
					userObj.setIsApproved(true);
					userObj.setApprovedBy(jwtFilter.getCurrentUser());
					String[] firstName = userObj.getOrganizerName().split(" ");
					String randomCode = UUID.randomUUID().toString().substring(0, 5);
					String token = randomCode.toUpperCase() + "@" + firstName[0].toUpperCase();
					userObj.setOrganizerId(token);
					organizerRepository.save(userObj);
					sendIdToAdmin(userObj.getOrganizerName(), requestMap.get("email"), token);
					return ProjectUtils.getResponseEntity("Approved successfully", HttpStatus.OK);

				} else {
					return ProjectUtils.getResponseEntity("Email not found!", HttpStatus.BAD_REQUEST);
				}

			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<>(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private void sendIdToAdmin(String name, String userEmail, String token) {
		try {
			emailUtils.sendApproveMessage(name, userEmail, token, "Latino Ecke- Account created", null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private boolean validateApproveMap(Map<String, String> requestMap) {
		boolean bool = requestMap.containsKey("email");
		return bool;
	}

	@Override
	public ResponseEntity<List<Organizer>> getApprovedOrganizers() {
		try {
			if (jwtFilter.isSuperAdmin() || jwtFilter.isAdmin()) {
				List<Organizer> userFromDb = organizerRepository.findByIsApproved(true);
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
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {

			if (validateForgotPasswordMap(requestMap)) {
				Organizer userObj = organizerRepository.findByEmail(requestMap.get("email"));
				if (Objects.nonNull(userObj)) {
					Otp otpObj = new Otp();
					String otp = generateOtp();
					otpObj.setEmail(userObj.getEmail());
					otpObj.setOtp(otp);
					otpObj.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
					otpRepository.save(otpObj);
					sendOtp(userObj.getOrganizerName(), requestMap.get("email"), otp);
					return ProjectUtils.getResponseEntity("Otp sent to your mail. It is valid for next 10 minutes",
							HttpStatus.OK);
				} else {
					return ProjectUtils.getResponseEntity("Incorrect E-mail! Contact team", HttpStatus.BAD_REQUEST);
				}
			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendOtp(String organizerName, String userEmail, String otp) {
		try {
			emailUtils.sendOtpMessage(organizerName, userEmail, otp, "Latino Ecke assistance", null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	private boolean validateForgotPasswordMap(Map<String, String> requestMap) {
		boolean bool = requestMap.containsKey("email");
		return bool;
	}

	@Override
	public ResponseEntity<String> resetPassword(Map<String, String> requestMap) {
		try {

			if (validateResetPassowrdMap(requestMap)) {
				Otp otpObj = otpRepository.findByEmail(requestMap.get("email"));
				Organizer userObj = organizerRepository.findByEmail(requestMap.get("email"));
				if (Objects.nonNull(otpObj)) {
					if (otpObj.getOtp().equalsIgnoreCase(requestMap.get("otp"))) {
						if (otpObj.getOtpExpiry().isAfter(LocalDateTime.now())) {

							userObj.setPassWord(requestMap.get("passWord"));
							otpObj.setOtp(null);
							otpObj.setOtpExpiry(null);
							organizerRepository.save(userObj);
							otpRepository.delete(otpObj);
						} else {
							organizerRepository.save(userObj);
							return ProjectUtils.getResponseEntity("OTP Expired!", HttpStatus.BAD_REQUEST);
						}

					} else {
						return ProjectUtils.getResponseEntity("Incorrect OTP!", HttpStatus.BAD_REQUEST);
					}
					return ProjectUtils.getResponseEntity("New password saved successfully", HttpStatus.OK);
				} else {
					return ProjectUtils.getResponseEntity("Email not found!", HttpStatus.BAD_REQUEST);
				}
			} else {
				return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateResetPassowrdMap(Map<String, String> requestMap) {
		boolean bool = requestMap.containsKey("otp") && requestMap.containsKey("email")
				&& requestMap.containsKey("passWord");
		return bool;
	}

}
