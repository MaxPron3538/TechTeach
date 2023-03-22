package main.controller;

import main.logic.entities.AnswerOption;
import main.logic.entities.Course;
import main.logic.entities.Lesson;
import main.logic.entities.TestPoint;
import main.logic.repositories.AnswerOptionRepository;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.LessonRepository;
import main.logic.repositories.TestPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("tests")
public class TestQuizRestController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TestPointRepository testPointRepository;

    @Autowired
    AnswerOptionRepository answerOptionRepository;

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getListOfTest(@PathVariable int courseId){
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny();

        if(optionalCourse.isPresent()) {
            return new ResponseEntity<>(optionalCourse.get().getTests(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint testPoint,@PathVariable int courseId){
        Optional<Course> optionalCourse = courseRepository.findAll().stream().filter(o -> o.getCourse_id() == courseId).findAny();

        if(optionalCourse.isPresent()){
            List<AnswerOption> answerOptions = testPoint.getAnswersOptions();
            answerOptions.forEach(o -> o.setTestPoint(testPoint));
            testPoint.setCourse(optionalCourse.get());
            testPointRepository.save(testPoint);
            answerOptions.forEach(o -> answerOptionRepository.save(o));

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
