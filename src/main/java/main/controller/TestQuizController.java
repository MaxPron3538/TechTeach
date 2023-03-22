package main.controller;

import main.logic.entities.Lesson;
import main.logic.entities.TestPoint;
import main.logic.repositories.LessonRepository;
import main.logic.repositories.TestPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint point, @RequestParam("name") String lessonName){
        return new ResponseEntity<>(testPointRepository.save(point),HttpStatus.OK);
    }
}
