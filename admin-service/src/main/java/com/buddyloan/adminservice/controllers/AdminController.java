package com.buddyloan.adminservice.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.buddyloan.adminservice.domain.Admin;
import com.buddyloan.adminservice.dto.AdminDto;
import com.buddyloan.adminservice.services.AdminService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    private AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @PostMapping("/admins")
    public ResponseEntity<Admin> createNewCustomer(@Valid @RequestBody AdminDto adminDto) {
        Admin adminActual = new Admin();
        adminActual.setEmployeeId(adminDto.getEmployeeId());
        adminActual.setName(adminDto.getName());
        adminActual.setEmail(adminDto.getEmail());
        adminActual.setPassword(adminDto.getPassword());
        adminActual.setImage(adminDto.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(adminActual));
    }
    
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok().body(adminService.findAll());
    }

    @GetMapping("/admins/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id) {
        Optional<Admin> admin = adminService.findById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok().body(admin.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such admin exist");
    }

    @GetMapping("/admins")
    @ResponseBody
    public ResponseEntity<?> getAdminByName(@RequestParam (required = false) String name) {
        if(name == null){
            return getAllAdmins();
        }
        else{
            Optional<Admin> admin = adminService.findByName(name);
            if (admin.isPresent()) {
                return ResponseEntity.ok().body(admin);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<?> updateAdminById(@PathVariable String id, @Valid @RequestBody AdminDto adminDto) {
        Optional<Admin> admin = adminService.findById(id);
        if(admin.isPresent()){
            admin.get().setName(adminDto.getName());
            admin.get().setEmail(adminDto.getEmail());
            admin.get().setEmployeeId(adminDto.getEmployeeId());
            admin.get().setPassword(adminDto.getPassword());
            admin.get().setImage(adminDto.getImage());
            return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(admin.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdminById(@PathVariable String id) {
        adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }


    @GetMapping("/admins/token")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody Admin user){
        user = adminService.findByIdAndPassword(user.getEmployeeId(), user.getPassword()).orElseThrow();
        Map<String, String> response = adminService.generateToken(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admins/authenticate")
    public ResponseEntity<Map<Object, Object>> authenticate(@Valid @RequestBody Admin user){
        Map<Object, Object> response = new HashMap<>();        
        HttpHeaders headers = new HttpHeaders();
        Optional<Admin> userExists = adminService.findById(user.getEmployeeId());
        if(userExists.isEmpty()){
            response.put("status", "FAILED");
            response.put("message", "No Admin Found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        try {
            ResponseEntity<Map<String, String>> loginResponse = login(user);
            String token = loginResponse.getBody().get("token");            
            headers.setBearerAuth(token);
            final Claims claims = Jwts.parser().setSigningKey("P@##$$kfjnsjkfd*")
                    .parseClaimsJws(token).getBody();
            response.put("status", "SUCCESS");
            response.put("name", claims.get("name"));
            response.put("email", claims.get("email"));
            response.put("employeeId", claims.get("employeeId"));
            response.put("password", claims.get("password"));
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (SignatureException e) {
            response.put("status", "FAILED");
            response.put("message", "Invalid Token");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NoSuchElementException e){
            response.put("status", "FAILED");
            response.put("message", "Invalid Password");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}
