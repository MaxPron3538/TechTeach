package main.logic.services;

import main.logic.entities.Course;
import main.logic.entities.Lesson;
import main.logic.entities.LessonKey;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseRepository courseRepository;

    public List<Lesson> getAllLessons(){
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLessonByContent(String content){
        return lessonRepository.findAll().stream().filter(s -> s.getName().contains(content) || s.getDescription().contains(content)).findFirst();
    }

    public Optional<Lesson> getLessonById(int id){
        return lessonRepository.findByIdLessonId(id);
    }

    public Optional<Lesson> addLesson(Lesson lesson,int courseId) {
        int lessonKey = lessonRepository.findAll().size() + 1;
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            lesson.setCourse(optionalCourse.get());
            lesson.setLessonId(new LessonKey(lessonKey, optionalCourse.get().getId()));
            lessonRepository.save(lesson);

            return Optional.of(lesson);
        }
        return Optional.empty();
    }

    public Optional<Lesson> updateLesson(Lesson lesson,int id){
        Optional<Lesson> optionalLesson = lessonRepository.findByIdLessonId(id);

        if(optionalLesson.isPresent()){
            Lesson updateLesson = optionalLesson.get();
            updateLesson.setName(lesson.getName());
            updateLesson.setDescription(lesson.getDescription());
            updateLesson.setVideoUrl(lesson.getVideoUrl());
            lessonRepository.save(updateLesson);

            return Optional.of(updateLesson);
        }
        return Optional.empty();
    }

    public Optional<Lesson> deleteLesson(int id){
        Optional<Lesson> optionalLesson = lessonRepository.findByIdLessonId(id);

        if (optionalLesson.isPresent()){
            lessonRepository.delete(optionalLesson.get());
            return optionalLesson;
        }
        return Optional.empty();
    }
}
