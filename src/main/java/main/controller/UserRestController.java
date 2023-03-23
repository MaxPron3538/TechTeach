package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.Course;
import main.logic.entities.CourseProgress;
import main.logic.entities.Lesson;
import main.logic.entities.User;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/courseProgresses")
    public ResponseEntity<List<CourseProgress>> getCourseProgress(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        List<CourseProgress> courseProgresses = userRepository.findByEmail(email).getCourseProgresses();
        return new ResponseEntity<>(courseProgresses,HttpStatus.OK);
    }

    @GetMapping("/courseProgresses/{id}")
    public ResponseEntity<CourseProgress> getCourseProgress(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Optional<CourseProgress> optionalCourseProgress = userRepository.findByEmail(email).getCourseProgresses().stream().filter(s -> s.getId() == id).findAny();

        return optionalCourseProgress.map(courseProgress -> new ResponseEntity<>(courseProgress, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("courses")
    public ResponseEntity<List<Course>> getCourses(@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        List<Course> courses = userRepository.findByEmail(email).getCourseProgresses().stream().map(CourseProgress::getCourse).collect(Collectors.toList());
        return new ResponseEntity<>(courses,HttpStatus.OK);
    }

    @GetMapping("courses/{id}")
    public ResponseEntity<Course> getCourse(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Course> optionalCourse = userRepository.findByEmail(email).getCourseProgresses()
                .stream().map(CourseProgress::getCourse).filter(o -> o.getCourse_id() == id).findAny();

        return optionalCourse.map(course -> new ResponseEntity<>(course, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @GetMapping("courses/{id}/lessons")
    public ResponseEntity<List<Lesson>> getLessons(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getCourse_id() == id).findAny();

        if(optionalCourse.isPresent()) {
            List<Lesson> lessons = userRepository.findByEmail(email).getCourseProgresses()
                    .stream().map(CourseProgress::getCourse).filter(o -> o.getCourse_id() == id)
                    .map(Course::getLessons).flatMap(List::stream).collect(Collectors.toList());

            return new ResponseEntity<>(lessons, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("courses/lessons/{name}")
    public ResponseEntity<Lesson> getLesson(@RequestHeader("Authorization") String token,@PathVariable String name){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Lesson> optionalLesson = userRepository.findByEmail(email).getCourseProgresses().stream().map(CourseProgress::getCourse)
                .map(Course::getLessons).flatMap(List::stream).filter(o -> o.getName().equals(name)).findAny();

        return optionalLesson.map(lesson -> new ResponseEntity<>(lesson, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("courses/{id}/lessons/{name}")
    public ResponseEntity<Lesson> getLesson(@RequestHeader("Authorization") String token,@PathVariable int id,@PathVariable String name){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getCourse_id() == id).findAny();

        if(optionalCourse.isPresent()) {
            Optional<Lesson> optionalLesson = userRepository.findByEmail(email).getCourseProgresses().stream().map(CourseProgress::getCourse)
                    .filter(s -> s.getCourse_id() == id).map(Course::getLessons).flatMap(List::stream).filter(o -> o.getName().equals(name)).findAny();

            return optionalLesson.map(lesson -> new ResponseEntity<>(lesson, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("courses/{id}/tests")
    public ResponseEntity<?> getListOfTest(@PathVariable int id){
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == id).findAny();

        if(optionalCourse.isPresent()) {
            return new ResponseEntity<>(optionalCourse.get().getTests(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
