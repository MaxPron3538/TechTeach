package main.controller;

import main.model.entities.Course;
import main.model.entities.User;
import main.model.repositories.UserRepository;
import main.model.validators.ValidatorUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession session;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody User user){
        User savedUser = userRepository.findAll().stream()
                .filter(s -> s.getEmail().equals(user.getEmail()) && s.getPasswordHash().equals(user.getPasswordHash())).findAny().orElse(null);
        if (savedUser != null) {
            return new ResponseEntity<>(savedUser,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user){
        User savedUser = userRepository.findAll().stream()
                .filter(s -> s.getEmail().equals(user.getEmail()) && s.getPasswordHash().equals(user.getPasswordHash())).findAny().orElse(null);

        if (savedUser == null) {
            if(ValidatorUserData.validateEmail(user.getEmail()) && ValidatorUserData.validatePassword(user.getPasswordHash())) {
                session.setAttribute("email",user.getEmail());
                session.setAttribute("passwordHash",user.getPasswordHash());
                userRepository.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(user,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }

    @GetMapping("/getSession")
    public ResponseEntity<?> getUserSession(){
        if(!session.isNew()) {
            User user = new User();
            user.setEmail(session.getAttribute("email").toString());
            user.setPasswordHash(session.getAttribute("passwordHash").toString());
            return new ResponseEntity<>(user,HttpStatus.FOUND);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}

