package com.buddyloan.userauthservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buddyloan.userauthservice.domain.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
    Optional<User> findByName(String name);

    Optional<User> findByAadhaarIdAndPassword(String aadhaarId, String password);
}
