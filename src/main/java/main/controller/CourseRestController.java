package main.controller;


import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.CourseProgressRepository;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.RegistrationRepository;
import main.logic.repositories.UserRepository;
import main.logic.services.CourseService;
import main.logic.services.JwtUserDetailsService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseRestController {

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("content/{content}")
    public ResponseEntity<?> getCourseByContent(@PathVariable String content){
        Optional<Course> optionalCourse = courseService.getCourseByContent(content);

        if (optionalCourse.isPresent()){
            return  new ResponseEntity<>(optionalCourse.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable int id){
        Optional<Course> optionalCourse = courseService.getCourseById(id);

        if(optionalCourse.isPresent()){
            return new ResponseEntity<>(optionalCourse.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<?> addCourse(@RequestBody Course course,@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = jwtUserDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            if(courseService.addCourse(course).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@RequestBody Course course, @RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = jwtUserDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            if(courseService.updateCourse(course,id).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = jwtUserDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {

            if(courseService.deleteCourse(id).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/submits/users/{id}")
    public ResponseEntity<?> submitStudentOnCourse(@RequestHeader("Authorization") String token,@PathVariable int id) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = jwtUserDetailsService.getUser(email);
        Optional<Course> optionalCourse = courseService.getCourseById(id);

        if(user.getRole() == Role.user) {
            if (optionalCourse.isPresent()) {
                if(courseService.submitStudentOnCourse(user,optionalCourse.get(),id)){
                    return new ResponseEntity<>(user,HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/submits/teachers/{id}")
    public ResponseEntity<?> submitTeacherOnCourse(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = jwtUserDetailsService.getUser(email);
        Optional<Course> optionalCourse = courseService.getCourseById(id);

        if(user.getRole() == Role.teacher) {
            if (optionalCourse.isPresent()) {
                if(courseService.submitTeacherOnCourse(user,optionalCourse.get())){
                    return new ResponseEntity<>(user,HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}