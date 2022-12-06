package com.buddyloan.userauthservice.services;
import com.buddyloan.userauthservice.domain.User;
import java.util.*;

public interface UserService {

    User save(User customer);

    List<User> findAll();

    Optional<User> findById(String id);

//    Optional<User> findByEmail( String email);

    Optional<User> findByName( String name);

    void deleteById( String id);

    Optional<User> findByIdAndPassword(String id, String password);
    Map<String, String> generateToken(User user);

    
}
