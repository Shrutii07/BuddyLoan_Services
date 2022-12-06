package com.buddyloan.userauthservice.services;
import com.buddyloan.userauthservice.repository.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.buddyloan.userauthservice.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User customer) {
        return this.userRepository.save(customer);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByIdAndPassword(String id, String password) {
        return userRepository.findByAadhaarIdAndPassword(id, password);
    }

    @Override
    public Map<String, String> generateToken(User user) {

        Map<String, String> response = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();

        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("aadhaarId", user.getAadhaarId());
        claims.put("password", user.getPassword());
        String token = Jwts.builder()
            .setSubject("Auth JWT")
            .setClaims(claims)
            .setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS256, "P@##$$kfjnsjkfd*")
            .compact();
        
        response.put("token", token);
        response.put("message", "SUCCESS");
        return response;
    }


}
