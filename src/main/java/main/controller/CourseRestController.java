package main.controller;


import main.model.entities.*;
import main.model.repositories.CourseProgressRepository;
import main.model.repositories.CourseRepository;
import main.model.repositories.RegistrationRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseRestController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseProgressRepository courseProgressRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @GetMapping("/")
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getCourseByName(@PathVariable String name){
        Course savedCourse = courseRepository.findAll().stream()
                    .filter(s -> s.getName().equals(name)).findFirst().orElse(null);
            if (savedCourse != null) {
                return new ResponseEntity<>(name,HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/{description}")
    public ResponseEntity<?> getCourseByDescription(@PathVariable String description) {
        Course savedCourse = courseRepository.findAll().stream()
                .filter(s -> s.getName().equals(description)).findFirst().orElse(null);
        if (savedCourse != null) {
            return new ResponseEntity<>(description,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody Course course){
        Course savedCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == course.getCourse_id()).findFirst().orElse(null);

        if(savedCourse == null){
            courseRepository.save(course);
            savedCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == course.getCourse_id()).findFirst().get();
            return new ResponseEntity<>(savedCourse,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }

    @GetMapping("/submit/student")
    public ResponseEntity<?> submitStudentOnCourse(@RequestParam("id_student") int studentId, @RequestParam("id_course") int courseId) {
        Optional<User> optionalUser = userRepository.findById(studentId).filter(s -> s.getRole().equals(Role.user));
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalUser.isPresent() && optionalCourse.isPresent()) {
            CourseProgress progress = courseProgressRepository.findAll().stream().filter(s -> s.getCourse().getId() == courseId).findAny().orElse(null);

            if(progress == null) {
                progress = new CourseProgress();
                progress.setCourse(optionalCourse.get());
                progress.setStudent(optionalUser.get());
                progress.setSubscriptionType(SubscriptionType.user);
                progress.setSubscribedAt(new Date());
                courseProgressRepository.save(progress);
                return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/submit/teacher")
    public ResponseEntity<?> submitTeacherOnCourse(@RequestParam("id_teacher") int teacherId, @RequestParam("id_course") int courseId){
        Optional<User> optionalUser = userRepository.findById(teacherId).filter(s -> s.getRole().equals(Role.teacher));
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if(optionalUser.isPresent() && optionalCourse.isPresent()){
            Registration registration = registrationRepository.findAll().stream()
                    .filter(s -> s.getTeacher().getId() == optionalUser.get().getId()
                            && s.getCourse().getCourse_id() == optionalCourse.get().getCourse_id()).findAny().orElse(null);

            if(registration == null) {
                registration = new Registration();
                registration.setId(new RegistrationKey(optionalUser.get().getId(), optionalCourse.get().getId()));
                registration.setTeacher(optionalUser.get());
                registration.setCourse(optionalCourse.get());
                registrationRepository.save(registration);
                User teacher = userRepository.findById(teacherId).get();

                return new ResponseEntity<>(teacher, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}