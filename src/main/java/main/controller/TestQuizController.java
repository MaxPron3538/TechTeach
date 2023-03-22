package main.controller;

import main.model.entities.Lesson;
import main.model.entities.TestPoint;
import main.model.repositories.LessonRepository;
import main.model.repositories.TestPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("tests")
public class TestQuizController {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    TestPointRepository testPointRepository;

    @GetMapping("/")
    public ResponseEntity<?> getListOfTest(@RequestParam("name") String lessonName){
        try {
            Lesson lesson = lessonRepository.findByName(lessonName);
            return new ResponseEntity<>(testPointRepository.findAll(), HttpStatus.OK);
        }catch (NullPointerException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint point,@RequestParam("name") String lessonName){
        return new ResponseEntity<>(testPointRepository.save(point),HttpStatus.OK);
    }
}
