package com.project.latinoEcke.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.latinoEcke.domain.Otp;

public interface OtpRepository extends MongoRepository<Otp, String>{

	Otp findByEmail(String email);

}
