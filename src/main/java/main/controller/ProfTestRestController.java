package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tests")
public class ProfTestRestController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TestPointRepository testPointRepository;

    @Autowired
    AnswerOptionRepository answerOptionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/")
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint testPoint,@RequestHeader("Authorization") String token,@RequestParam("id_course") int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getCourse_id() == courseId).findAny();

            if (optionalCourse.isPresent()) {
                List<AnswerOption> answerOptions = testPoint.getAnswersOptions();
                answerOptions.forEach(o -> o.setTestPoint(testPoint));
                testPoint.setCourse(optionalCourse.get());
                testPointRepository.save(testPoint);
                answerOptions.forEach(o -> answerOptionRepository.save(o));

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTestPoint(@RequestBody TestPoint testPoint,@RequestHeader("Authorization") String token,@PathVariable int id){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<TestPoint> optionalTestPoint = testPointRepository.findById(id);

            if(optionalTestPoint.isPresent()){
                List<AnswerOption> answerOptions = testPoint.getAnswersOptions();
                answerOptions.forEach(o -> o.setTestPoint(testPoint));
                testPointRepository.save(testPoint);
                answerOptions.forEach(o -> answerOptionRepository.save(o));

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTestPoint(@RequestBody TestPoint testPoint,@RequestHeader("Authorization") String token,@RequestParam("id_course") int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getCourse_id() == courseId).findAny();

            if (optionalCourse.isPresent()) {
                List<AnswerOption> answerOptions = testPoint.getAnswersOptions();
                answerOptions.forEach(o -> o.setTestPoint(testPoint));
                testPoint.setCourse(optionalCourse.get());
                testPointRepository.save(testPoint);
                answerOptions.forEach(o -> answerOptionRepository.save(o));

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
