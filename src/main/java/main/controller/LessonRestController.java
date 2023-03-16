package main.controller;

import main.model.entities.Course;
import main.model.entities.Lesson;
import main.model.entities.LessonKey;
import main.model.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LessonRestController {

    @Autowired
    LessonRepository lessonRepository;

    @PostMapping
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson, @RequestBody Course course){
        lesson.setLessonId(new LessonKey(course.getId()));
        lessonRepository.save(lesson);
        Lesson saveLesson =lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findFirst().orElse(null);

        return new ResponseEntity<>(saveLesson, HttpStatus.OK);
    }

}
