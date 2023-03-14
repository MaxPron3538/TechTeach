package main.controller;

import main.model.entities.*;
import main.model.repositories.CourseRepository;
import main.model.repositories.RegistrationRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import java.util.Optional;

@RestController
@RequestMapping("/relate")
public class AdminRestController {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/teacher")
    public ResponseEntity<?> relateTeacherWithCourse(@RequestParam("id_teacher") int teacherId, @RequestParam("id_course") int courseId,Registration registration){
         Optional<User> optionalUser = userRepository.findById(teacherId).filter(s -> s.getRole().equals(Role.teacher));
         Optional<Course> optionalCourse = courseRepository.findById(courseId);

         if(optionalUser.isPresent() && optionalCourse.isPresent()){
             registration.setId(new RegistrationKey(optionalUser.get().getId(),optionalCourse.get().getId()));
             registration.setTeacher(optionalUser.get());
             registration.setCourse(optionalCourse.get());
             registrationRepository.save(registration);

             return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
         }
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
