package main.controller;


import main.model.entities.Course;
import main.model.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/")
public class CourseRestController {

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("/name")
    public ResponseEntity<?> CourseByName(@RequestParam String name){
        Course savedCourse = courseRepository.findAll().stream()
                    .filter(s -> s.getName().equals(name)).findFirst().orElse(null);
            if (savedCourse != null) {
                return new ResponseEntity<>(name,HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }
    @PostMapping("/description")
    public ResponseEntity<?> CourseByDescription(@RequestParam String description) {
        Course savedCourse = courseRepository.findAll().stream()
                .filter(s -> s.getName().equals(description)).findFirst().orElse(null);
        if (savedCourse != null) {
            return new ResponseEntity<>(description,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }
}