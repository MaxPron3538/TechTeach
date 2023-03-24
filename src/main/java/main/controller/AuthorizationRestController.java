package main.controller;

import main.logic.JwtConfigs.JwtRequest;
import main.logic.JwtConfigs.JwtResponse;
import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.User;
import main.logic.models.UserModel;
import main.logic.repositories.UserRepository;
import main.logic.services.JwtUserDetailsService;
import main.logic.validators.ValidatorUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthorizationRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody JwtRequest request) throws Exception {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserModel user) {
        User savedUser = userRepository.findByEmail(user.getEmail());

        if (savedUser == null) {
            if (ValidatorUserData.validateEmail(user.getEmail()) && ValidatorUserData.validatePassword(user.getPasswordHash())) {
                userDetailsService.save(user);

                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                final String token = jwtTokenUtil.generateToken(userDetails);

                return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
            }
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user,HttpStatus.ALREADY_REPORTED);
    }

}

