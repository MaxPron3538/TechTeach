package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.Course;
import main.logic.entities.CourseProgress;
import main.logic.entities.Lesson;
import main.logic.entities.User;
import main.logic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("users")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        User user = userRepository.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/courseProgresses")
    public ResponseEntity<List<CourseProgress>> getCourseProgress(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<CourseProgress> courseProgresses = userRepository.findByEmail(email).getCourseProgresses();
        return new ResponseEntity<>(courseProgresses,HttpStatus.OK);
    }

    @GetMapping("/courseProgresses{id}")
    public ResponseEntity<CourseProgress> getCourseProgress(@RequestHeader("Authorization") String token,@RequestHeader("id") int courseProgressId){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));

        try {
            CourseProgress progress = userRepository.findByEmail(email).getCourseProgresses().get(courseProgressId);
            return new ResponseEntity<>(progress,HttpStatus.OK);

        }catch (NullPointerException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("courses")
    public ResponseEntity<List<Course>> getCourses(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<Course> courses = userRepository.findByEmail(email).getCourses();
        return new ResponseEntity<>(courses,HttpStatus.OK);
    }

    @GetMapping("courses/lessons")
    public ResponseEntity<List<Lesson>> getLessons(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<Lesson> lessons = userRepository.findByEmail(email).getCourses()
                .stream().map(Course::getLessons).flatMap(List::stream).collect(Collectors.toList());
        return new ResponseEntity<>(lessons,HttpStatus.OK);
    }

    @GetMapping("courses/{courseId}")
    public ResponseEntity<Course> getCourse(@RequestHeader("Authorization") String token,@PathVariable int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        Course course = userRepository.findByEmail(email).getCourses()
                .stream().filter(o -> o.getCourse_id() == courseId).findAny().orElse(null);

        if(course != null){
            return new ResponseEntity<>(course,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("courses/lessons/{name}")
    public ResponseEntity<Lesson> getLesson(@RequestHeader("Authorization") String token,@PathVariable String name){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        Lesson lesson = userRepository.findByEmail(email).getCourses()
                .stream().map(Course::getLessons).flatMap(List::stream).filter(o -> o.getName().equals(name)).findAny().orElse(null);

        if(lesson != null){
            return new ResponseEntity<>(lesson,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
