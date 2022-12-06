package com.buddyloan.adminservice.services;

import com.buddyloan.adminservice.domain.Admin;
import java.util.*;

public interface AdminService {
    Admin save(Admin admin);

    List<Admin> findAll();

    Optional<Admin> findById(String id);

    Optional<Admin> findByName( String name);

    void deleteById( String id);
    
    Optional<Admin> findByIdAndPassword(String id, String password);
    Map<String, String> generateToken(Admin user);
}
