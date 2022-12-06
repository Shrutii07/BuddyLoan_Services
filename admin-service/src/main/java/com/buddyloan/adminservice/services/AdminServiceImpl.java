package com.buddyloan.adminservice.services;

import com.buddyloan.adminservice.repository.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.buddyloan.adminservice.domain.Admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
    private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin save(Admin admin) {
        return this.adminRepository.save(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> findByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public void deleteById(String id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Optional<Admin> findByIdAndPassword(String id, String password) {
        return adminRepository.findByEmployeeIdAndPassword(id, password);
    }

    @Override
    public Map<String, String> generateToken(Admin user) {

        Map<String, String> response = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();

        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("employeeId", user.getEmployeeId());
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
