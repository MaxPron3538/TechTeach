package main.controller;

import main.model.JwtConfigs.JwtTokenUtil;
import main.model.entities.*;
import main.model.repositories.CourseRepository;
import main.model.repositories.LessonRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class LessonRestController {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/lessons")
    public List<Lesson> getAllCourses(){
        return lessonRepository.findAll();
    }

    @PostMapping("/lessons")
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson,@RequestHeader("Authorization") String token,@RequestParam("id_course") int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")+1));
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Lesson saveLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findAny().orElse(null);

            if (saveLesson == null) {
                int lessonKey = lessonRepository.findAll().size() + 1;
                Course saveCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny().orElse(null);

                if (saveCourse != null) {
                    lesson.setCourse(saveCourse);
                    lesson.setLessonId(new LessonKey(lessonKey, courseId));
                    lessonRepository.save(lesson);
                    saveLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findFirst().get();

                    return new ResponseEntity<>(saveLesson, HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @DeleteMapping("/alllessons{name}")
    public ResponseEntity<?> deleteLesson(@PathVariable String name,@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")+1));
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher){
            try {
                Lesson lesson = lessonRepository.findByName(name);
                lessonRepository.delete(lesson);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (NullPointerException ex){
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/alllessons/{content}")
    public ResponseEntity<?> getLessonByName(@PathVariable String content){
        Optional<Lesson> optionalLesson = lessonRepository.findAll()
                .stream().filter(s -> s.getName().contains(content) && s.getDescription().contains(content)).findAny();

        if(optionalLesson.isPresent()){
            return new ResponseEntity<>(optionalLesson.get(),HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
