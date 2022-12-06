package com.buddyloan.adminservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buddyloan.adminservice.domain.Admin;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String>{
    Optional<Admin> findByName(String name);

    Optional<Admin>  findByEmployeeIdAndPassword(String employeeId, String password);
}
