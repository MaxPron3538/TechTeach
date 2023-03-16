package main.controller;

import main.model.entities.Course;
import main.model.entities.Lesson;
import main.model.entities.LessonKey;
import main.model.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class LessonRestController {

    @Autowired
    LessonRepository lessonRepository;

    @PostMapping("/lesson/")
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson, @RequestParam int courseId){
        Lesson saveLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findFirst().orElse(null);
        if(saveLesson == null){
            saveLesson = new Lesson();
            lesson.setLessonId(new LessonKey(courseId));
            lessonRepository.save(lesson);

            return new ResponseEntity<>(saveLesson, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }

}
