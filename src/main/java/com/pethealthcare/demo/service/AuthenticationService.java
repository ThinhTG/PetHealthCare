package com.pethealthcare.demo.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.pethealthcare.demo.dto.request.AuthenticationRequest;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.UserRepository;
import lombok.experimental.NonFinal;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserRepository userRepository;

    @NonFinal
    @Value("${signerKey}")
    protected String SIGNER_KEY;

    public String authenticate(AuthenticationRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail());
        if (exists) {
            User user = userRepository.findByEmail(request.getEmail());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (match) {
                var token = generateToken(request.getEmail());
                return token;
            }
        }
        return null;
    }

    private String generateToken(String email) {
        User user = userRepository.findByEmail(email);
        JSONObject userJson = new JSONObject();
        userJson.put("userID", user.getUserId());
        userJson.put("name", user.getName());
        userJson.put("email", user.getEmail());
        userJson.put("phone", user.getPhone());
        userJson.put("role", user.getRole());

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .claim("User", userJson)
                .issuer("http://pethealthcare.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create JWT object", e);
            throw new RuntimeException(e);
        }
    }
}
