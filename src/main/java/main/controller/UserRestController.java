package main.controller;

import main.model.JwtConfigs.JwtTokenUtil;
import main.model.entities.Course;
import main.model.entities.CourseProgress;
import main.model.entities.Lesson;
import main.model.entities.User;
import main.model.repositories.CourseRepository;
import main.model.repositories.LessonRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("users")
public class UserRestController {

    @Autowired
    UserRepository repositoryUser;

    @Autowired
    CourseRepository repositoryCourse;

    @Autowired
    LessonRepository repositoryLesson;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @GetMapping("/")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        User user = repositoryUser.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/courseProgresses")
    public ResponseEntity<List<CourseProgress>> getCourseProgress(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<CourseProgress> courseProgresses = repositoryUser.findByEmail(email).getCourseProgresses();
        return new ResponseEntity<>(courseProgresses,HttpStatus.OK);
    }

    @GetMapping("/courseProgresses{id}")
    public ResponseEntity<CourseProgress> getCourseProgress(@RequestHeader("Authorization") String token,@RequestHeader("id") int courseProgressId){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));

        try {
            CourseProgress progress = repositoryUser.findByEmail(email).getCourseProgresses().get(courseProgressId);
            return new ResponseEntity<>(progress,HttpStatus.OK);

        }catch (NullPointerException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("courses")
    public ResponseEntity<List<Course>> getCourses(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<Course> courses = repositoryUser.findByEmail(email).getCourses();
        return new ResponseEntity<>(courses,HttpStatus.OK);
    }

    @GetMapping("courses/lessons")
    public ResponseEntity<List<Lesson>> getLessons(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        List<Lesson> lessons = repositoryUser.findByEmail(email).getCourses()
                .stream().map(Course::getLessons).flatMap(List::stream).collect(Collectors.toList());
        return new ResponseEntity<>(lessons,HttpStatus.OK);
    }

    @GetMapping("courses/{id}")
    public ResponseEntity<Course> getCourse(@RequestHeader("Authorization") String token,@RequestParam("id") int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        Course course = repositoryUser.findByEmail(email).getCourses()
                .stream().filter(o -> o.getCourse_id() == courseId).findAny().orElse(null);

        if(course != null){
            return new ResponseEntity<>(course,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("courses/lessons/{name}")
    public ResponseEntity<Lesson> getLesson(@RequestHeader("Authorization") String token,@RequestParam("name") String name){
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(token.indexOf(" ")));
        Lesson lesson = repositoryUser.findByEmail(email).getCourses()
                .stream().map(Course::getLessons).flatMap(List::stream).filter(o -> o.getName().equals(name)).findAny().orElse(null);

        if(lesson != null){
            return new ResponseEntity<>(lesson,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
