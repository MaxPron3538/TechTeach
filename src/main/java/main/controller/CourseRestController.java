package main.controller;


import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.CourseProgressRepository;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.RegistrationRepository;
import main.logic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class CourseRestController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseProgressRepository courseProgressRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/courses")
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{content}")
    public ResponseEntity<?> getCourseByName(@PathVariable String content){
        Course savedCourse = courseRepository.findAll().stream()
                    .filter(s -> s.getName().contains(content) || s.getDescription().contains(content)).findFirst().orElse(null);

        if(savedCourse != null) {
            return new ResponseEntity<>(savedCourse, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/courses")
    public ResponseEntity<?> addCourse(@RequestBody Course course,@RequestHeader("Authorization") String token){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Course savedCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == course.getCourse_id()).findFirst().orElse(null);

            if (savedCourse == null) {
                courseRepository.save(course);
                savedCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == course.getCourse_id()).findFirst().get();
                return new ResponseEntity<>(savedCourse, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @PutMapping("/courses{id}")
    public ResponseEntity<?> updateCourse(@RequestBody Course course, @RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == id).findAny();

            if (optionalCourse.isPresent()) {
                Course savedCourse = optionalCourse.get();
                savedCourse.setName(course.getName());
                savedCourse.setDescription(course.getDescription());
                savedCourse.setLessonProgress(course.getLessonProgress());
                courseRepository.save(savedCourse);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == id).findAny();

            if(optionalCourse.isPresent()){
                courseRepository.deleteById(optionalCourse.get().getId());
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }

    @GetMapping("/courses/submit/student/{courseId}")
    public ResponseEntity<?> submitStudentOnCourse(@RequestHeader("Authorization") String token,@PathVariable int courseId) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny();

        if(user.getRole() == Role.user) {
            if (optionalCourse.isPresent()) {
                CourseProgress progress = courseProgressRepository.findAll().stream().filter(s -> s.getCourse().getCourse_id() == courseId).findAny().orElse(null);

                if (progress == null) {
                    progress = new CourseProgress();
                    progress.setCourse(optionalCourse.get());
                    progress.setStudent(user);
                    progress.setSubscriptionType(SubscriptionType.user);
                    progress.setSubscribedAt(LocalDate.now());
                    int month = LocalDate.now().getMonth().getValue() + (int) (Math.random() * 12 - LocalDate.now().getMonth().getValue());
                    int day = 1 + (int) (Math.random() * 28);
                    progress.setValid_until(LocalDate.of(LocalDate.now().getYear(),
                            LocalDate.now().getMonthValue() + month, day));
                    courseProgressRepository.save(progress);

                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/courses/submit/teacher/{courseId}")
    public ResponseEntity<?> submitTeacherOnCourse(@RequestHeader("Authorization") String token,@PathVariable int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny();

        if(user.getRole() == Role.teacher) {
            if (optionalCourse.isPresent()) {
                Registration registration = registrationRepository.findAll().stream()
                        .filter(s -> s.getTeacher().getId() == user.getId()
                                && s.getCourse().getCourse_id() == optionalCourse.get().getCourse_id()).findAny().orElse(null);

                if (registration == null) {
                    registration = new Registration();
                    registration.setId(new RegistrationKey(user.getId(), optionalCourse.get().getId()));
                    registration.setTeacher(user);
                    registration.setCourse(optionalCourse.get());
                    registrationRepository.save(registration);

                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
}