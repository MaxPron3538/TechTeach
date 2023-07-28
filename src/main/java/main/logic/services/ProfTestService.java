package main.logic.services;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.*;
import main.logic.repositories.AnswerOptionRepository;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.TestPointRepository;
import main.logic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProfTestService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TestPointRepository testPointRepository;

    @Autowired
    AnswerOptionRepository answerOptionRepository;

    public List<TestPoint> getAllTests(){
        return testPointRepository.findAll();
    }

    public Optional<TestPoint> addTestPoint(TestPoint testPoint,int courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            testPoint.setCourse(optionalCourse.get());
            testPointRepository.save(testPoint);

            return Optional.of(testPoint);
        }
        return Optional.empty();
    }

    public Optional<TestPoint> updateTestPoint(List<AnswerOption> answerOptions,int testId){
        Optional<TestPoint> optionalTestPoint = testPointRepository.findById(testId);

        if (optionalTestPoint.isPresent()) {
            answerOptions.forEach(o -> o.setTestPoint(optionalTestPoint.get()));
            answerOptionRepository.saveAll(answerOptions);

            return optionalTestPoint;
        }
        return Optional.empty();
    }

    public Optional<TestPoint> getTestPointById(int id){
        return testPointRepository.findById(id);
    }

    public Optional<TestPoint> deleteTestPoint(int testId){
        Optional<TestPoint> optionalTestPoint = testPointRepository.findById(testId);

        if (optionalTestPoint.isPresent()) {
            List<AnswerOption> answerOptions = optionalTestPoint.get().getAnswerOptions();
            answerOptionRepository.deleteAll(answerOptions);
            testPointRepository.delete(optionalTestPoint.get());
            return optionalTestPoint;
        }
        return Optional.empty();
    }

    public List<TestPoint> deleteAllTestByCourseId(int courseId){
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            List<TestPoint> testPoints = optionalCourse.get().getTests();

            for (TestPoint t : testPoints) {
                List<AnswerOption> answerOptions = t.getAnswerOptions();
                answerOptionRepository.deleteAll(answerOptions);
                testPointRepository.delete(t);
            }
            return testPoints;
        }
        return Collections.emptyList();
    }
}
