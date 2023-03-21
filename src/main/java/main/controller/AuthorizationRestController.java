package main.controller;

import main.model.JwtConfigs.JwtRequest;
import main.model.JwtConfigs.JwtResponse;
import main.model.JwtConfigs.JwtTokenUtil;
import main.model.entities.User;
import main.model.models.UserModel;
import main.model.repositories.UserRepository;
import main.model.services.JwtUserDetailsService;
import main.model.validators.ValidatorUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

