package main.controller;

import main.model.entities.User;
import main.model.repositories.UserRepository;
import main.model.validators.ValidatorUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody User user){
        User savedUser = userRepository.findAll().stream()
                .filter(s -> s.getEmail().equals(user.getEmail()) && s.getPasswordHash().equals(user.getPasswordHash())).findAny().orElse(null);

        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user){
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (!optionalUser.isPresent()) {
            if(ValidatorUserData.validateEmail(user.getEmail()) && ValidatorUserData.validatePassword(user.getPasswordHash())) {
                userRepository.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}

