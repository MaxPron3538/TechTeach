package main.controller;

import main.model.entities.Course;
import main.model.entities.Lesson;
import main.model.entities.LessonKey;
import main.model.repositories.CourseRepository;
import main.model.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class LessonRestController {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("/lessons")
    public ResponseEntity<?> addLesson(@RequestBody Lesson lesson,@RequestParam("id_course") int courseId,@RequestParam("id_lesson") int lessonId){
        Lesson saveLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findAny().orElse(null);

        if(saveLesson == null){
            Course saveCourse = courseRepository.findAll().stream().filter(s -> s.getCourse_id() == courseId).findAny().orElse(null);

            if(saveCourse != null) {
                lesson.setCourse(saveCourse);
                lesson.setLessonId(new LessonKey(lessonId,courseId));
                lessonRepository.save(lesson);
                saveLesson = lessonRepository.findAll().stream().filter(s -> s.getName().equals(lesson.getName())).findFirst().get();

                return new ResponseEntity<>(saveLesson, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
    }

    @GetMapping("/lessons/{content}")
    public ResponseEntity<?> getLessonByName(@PathVariable String content){
        Optional<Lesson> optionalLesson = lessonRepository.findAll()
                .stream().filter(s -> s.getName().contains(content) && s.getDescription().contains(content)).findAny();

        if(optionalLesson.isPresent()){
            return new ResponseEntity<>(optionalLesson.get(),HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
