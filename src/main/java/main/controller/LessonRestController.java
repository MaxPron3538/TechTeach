package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.LessonRepository;
import main.logic.repositories.UserRepository;
import main.logic.services.JwtUserDetailsService;
import main.logic.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonRestController {


    @Autowired
    LessonService lessonService;

    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public List<Lesson> getAllLessons(){
        return lessonService.getAllLessons();
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson,@RequestHeader("Authorization") String token,@PathVariable int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            Optional<Lesson> optionalLesson = lessonService.getLessonByContent(lesson.getName());

            if(optionalLesson.isPresent()){
                if(lessonService.addLesson(lesson,courseId).isPresent()){
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@RequestBody Lesson lesson,@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher){
            if(lessonService.updateLesson(lesson,id).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher){
            if(lessonService.deleteLesson(id).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getLessonById(@RequestHeader("Authorization") String token,@PathVariable int id) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if (user.getRole() == Role.teacher) {
            Optional<Lesson> optionalLesson = lessonService.getLessonById(id);

            if(optionalLesson.isPresent()){
                return  new ResponseEntity<>(optionalLesson.get(),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("content/{content}")
    public ResponseEntity<?> getLessonByContent(@RequestHeader("Authorization") String token,@PathVariable String content){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            Optional<Lesson> optionalLesson = lessonService.getLessonByContent(content);

            if(optionalLesson.isPresent()){
                return new ResponseEntity<>(optionalLesson.get(),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

