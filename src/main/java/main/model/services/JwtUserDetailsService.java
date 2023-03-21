package main.model.services;

import main.model.entities.User;
import main.model.models.UserModel;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
                new ArrayList<>());
    }

    public User save(UserModel user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPasswordHash(bcryptEncoder.encode(user.getPasswordHash()));
        newUser.setDisplayName(user.getDisplayName());
        newUser.setRole(user.getRole());
        return repository.save(newUser);
    }
}
