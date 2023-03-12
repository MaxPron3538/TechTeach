package main.controller;

import main.model.User;
import org.springframework.web.bind.annotation.*;

//Hello Max
@RestController
@RequestMapping("/")
public class UserRestController {

    @PostMapping("/signIn")
    public User signIn(@RequestBody User user){

        return new User();
    }

    @PostMapping("/signUp")
    public User signUp(@RequestBody User user){

        return new User();
    }
}
