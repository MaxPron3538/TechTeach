package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tests")
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

    @GetMapping("")
    public List<TestPoint> getAllTestPoints(){
        return testPointRepository.findAll();
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint testPoint, @RequestHeader("Authorization") String token,@PathVariable int courseId) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getId() == courseId).findAny();

            if (optionalCourse.isPresent()) {
                testPoint.setCourse(optionalCourse.get());
                testPointRepository.save(testPoint);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/optiones/{testId}")
    public ResponseEntity<?> updateTestPoint(@RequestBody List<AnswerOption> answerOptions, @RequestHeader("Authorization") String token,@PathVariable int testId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<TestPoint> optionalTestPoint = testPointRepository.findAll().stream().filter(o -> o.getId() == testId).findAny();

            if (optionalTestPoint.isPresent()) {
                answerOptions.forEach(o -> o.setTestPoint(optionalTestPoint.get()));
                answerOptionRepository.saveAll(answerOptions);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteTestPoint(@RequestHeader("Authorization") String token,@PathVariable int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user.getRole() == Role.teacher) {
            Optional<TestPoint> optionalTestPoint = testPointRepository.findById(courseId);

            if (optionalTestPoint.isPresent()) {
                List<AnswerOption> answerOptions = optionalTestPoint.get().getAnswerOptions();
                answerOptionRepository.deleteAll(answerOptions);
                testPointRepository.delete(optionalTestPoint.get());

                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}


