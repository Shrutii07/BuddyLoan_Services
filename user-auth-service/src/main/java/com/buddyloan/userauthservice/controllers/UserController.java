package com.buddyloan.userauthservice.controllers;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.buddyloan.userauthservice.domain.User;
import com.buddyloan.userauthservice.dto.UserDto;
import com.buddyloan.userauthservice.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/customers")
    public ResponseEntity<User> createNewCustomer(@Valid @RequestBody UserDto userDto) {
        User userActual = new User();
        userActual.setAadhaarId(userDto.getAadhaarId());
        userActual.setName(userDto.getName());
        userActual.setEmail(userDto.getEmail());
        userActual.setFeedback(userDto.getFeedback());
        userActual.setPassword(userDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userActual));
    }

    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        Optional<User> customer = userService.findById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok().body(customer.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such user exist");
    }

    @GetMapping("/customers/{id}/{password}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id, @PathVariable String password) {
        Optional<User> customer = userService.findByIdAndPassword(id,password);
        if (customer.isPresent()) {
            return ResponseEntity.ok().body(customer.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such user exist");
    }



    @GetMapping("/customers")
    @ResponseBody
    public ResponseEntity<?> getCustomerByName(@RequestParam (required = false) String name) {
        if(name == null){
            return getAllCustomers();
        }
        else{
            Optional<User> customer = userService.findByName(name);
            if (customer.isPresent()) {
                return ResponseEntity.ok().body(customer);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomerById(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        Optional<User> user = userService.findById(id);
        if(user.isPresent()){
            user.get().setName(userDto.getName());
            user.get().setEmail(userDto.getEmail());
            user.get().setAadhaarId(userDto.getAadhaarId());
            user.get().setPassword(userDto.getPassword());
            user.get().setFeedback(userDto.getFeedback());
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/customers/feedback/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable String id, @RequestBody UserDto userDto) {
        Optional<User> user = userService.findById(id);
        if(user.isPresent()){
            user.get().setFeedback(userDto.getFeedback());
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    @GetMapping("/customers/token")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody User user){
        user = userService.findByIdAndPassword(user.getAadhaarId(), user.getPassword()).orElseThrow();
        Map<String, String> response = userService.generateToken(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/authenticate")
    public ResponseEntity<Map<Object, Object>> authenticate(@Valid @RequestBody User user){
        Map<Object, Object> response = new HashMap<>();        
        HttpHeaders headers = new HttpHeaders();
        Optional<User> userExists = userService.findById(user.getAadhaarId());
        if(userExists.isEmpty()){
            response.put("status", "FAILED");
            response.put("message", "No User Found");
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
            response.put("aadhaarId", claims.get("aadhaarId"));
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
