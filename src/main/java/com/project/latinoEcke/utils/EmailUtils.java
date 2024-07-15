package com.project.latinoEcke.utils;

import java.util.List;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendSimpleMessage(String name, String to, String token, String subject, List<String> emailList) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(message, true);
		helper.setFrom("livelifelikebee@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); padding: 20px; max-width: 600px; margin: 20px auto; }" +
                "h2 { color: #333333; }" +
                "p { color: #555555; }" +
                "a { color: #1a73e8; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                ".footer { margin-top: 20px; font-size: 12px; color: #aaaaaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<h2><b>Hello "+name+",</b></h2>" +
                "<h3>Below is your Admin Id Details for Latino Ecke</h3>" +
                "<h3>Use your ID while signUp</h3>" +
                "<p><b>Admin ID:</b> " + token + "</p>" +
                "<p><b>Keep in your mind that your ID is case sensitive.</b></p>"+
                "<p><b>Incase of any issues while signing Up, kindly reach out to us!</b></p>"+
                "<p><b>Regards,</b></p>"+
                "<p><b>LATINO ECKE:)</b></p>"+
                "</div>" +
                "<div class=\"footer\">" +
                "<p>This is an automated message. Please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
		message.setContent(htmlMsg,"text/html");
		if(Objects.nonNull(emailList)&& !emailList.isEmpty()) {
			helper.setCc(getCcArray(emailList));
		}
		
		javaMailSender.send(message);
		
	}

	private String[] getCcArray(List<String> ccList) {
		String[] cc= new String[ccList.size()];
		for(int i=0; i<ccList.size(); i++) {
			cc[i]= ccList.get(i);
		}
		return cc;
	}
	
	public void sendApproveMessage(String name, String to, String token, String subject, List<String> emailList) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(message, true);
		helper.setFrom("livelifelikebee@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); padding: 20px; max-width: 600px; margin: 20px auto; }" +
                "h2 { color: #333333; }" +
                "p { color: #555555; }" +
                "a { color: #1a73e8; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                ".footer { margin-top: 20px; font-size: 12px; color: #aaaaaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<h2><b>Hello "+name+",</b></h2>" +
                "<h3>Below is your Organizer Id Details for Latino Ecke</h3>" +
                "<h3>Use your ID and Password while login</h3>" +
                "<p><b>Organizer ID:</b> " + token + "</p>" +
                "<p><b>Keep in your mind that your ID is case sensitive.</b></p>"+
                "<p><b>Incase of any issues while logging in, kindly reach out to us!</b></p>"+
                "<p><b>Regards,</b></p>"+
                "<p><b>LATINO ECKE:)</b></p>"+
                "</div>" +
                "<div class=\"footer\">" +
                "<p>This is an automated message. Please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
		message.setContent(htmlMsg,"text/html");
		if(Objects.nonNull(emailList)&& !emailList.isEmpty()) {
			helper.setCc(getCcArray(emailList));
		}
		
		javaMailSender.send(message);
		
	}
	
	public void forgotMail(String to, String subject, String password) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(message, true);
		helper.setFrom("livelifelikebee@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		message.setContent(htmlMsg,"text/html");
		javaMailSender.send(message);
		
		
	}
	
	public void sendOtpMessage(String name, String to, String token, String subject, List<String> emailList) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(message, true);
		helper.setFrom("livelifelikebee@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); padding: 20px; max-width: 600px; margin: 20px auto; }" +
                "h2 { color: #333333; }" +
                "p { color: #555555; }" +
                "a { color: #1a73e8; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                ".footer { margin-top: 20px; font-size: 12px; color: #aaaaaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<h2><b>Hello "+name+",</b></h2>" +
                "<h3>Below is your OTP Details to reset your password</h3>" +
                "<h3>Your OTP is valid for next 10 minutes</h3>" +
                "<p><b>OTP:</b> " + token + "</p>" +
//                "<p><b>Keep in your mind that your ID is case sensitive.</b></p>"+
//                "<p><b>Incase of any issues while logging in, kindly reach out to us!</b></p>"+
                "<p><b>Regards,</b></p>"+
                "<p><b>LATINO ECKE:)</b></p>"+
                "</div>" +
                "<div class=\"footer\">" +
                "<p>This is an automated message. Please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
		message.setContent(htmlMsg,"text/html");
		if(Objects.nonNull(emailList)&& !emailList.isEmpty()) {
			helper.setCc(getCcArray(emailList));
		}
		
		javaMailSender.send(message);
		
	}

}
