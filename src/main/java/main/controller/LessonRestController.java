package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.LessonRepository;
import main.logic.repositories.UserRepository;
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
    LessonRepository lessonRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public List<Lesson> getAllLessons(){
        return lessonRepository.findAll();
    }

    @PostMapping("")
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson,@RequestHeader("Authorization") String token,@RequestParam("id_course") int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Lesson> optionalLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findAny();

            if (!optionalLesson.isPresent()) {
                int lessonKey = lessonRepository.findAll().size() + 1;
                Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny();

                if (optionalCourse.isPresent()) {
                    lesson.setCourse(optionalCourse.get());
                    lesson.setLessonId(new LessonKey(lessonKey, optionalCourse.get().getId()));
                    lessonRepository.save(lesson);

                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateLesson(@PathVariable String name,@RequestBody Lesson lesson,@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher){
            Lesson updateLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(name)).findAny().orElse(null);

            if(updateLesson != null){
                updateLesson.setName(lesson.getName());
                updateLesson.setDescription(lesson.getDescription());
                updateLesson.setVideoUrl(lesson.getVideoUrl());
                lessonRepository.save(updateLesson);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteLesson(@PathVariable String name,@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher){
            Optional<Lesson> optionalLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(name)).findAny();

            if(optionalLesson.isPresent()){
                lessonRepository.delete(optionalLesson.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{content}")
    public ResponseEntity<?> getLessonByContent(@PathVariable String content){
        Optional<Lesson> optionalLesson = lessonRepository.findAll()
                .stream().filter(s -> s.getName().contains(content) || s.getDescription().contains(content)).findAny();

        if(optionalLesson.isPresent()){
            return new ResponseEntity<>(optionalLesson.get(),HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
