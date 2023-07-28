package main.logic.repositories;

import main.logic.entities.Lesson;
import main.logic.entities.LessonKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, LessonKey> {
    List<Lesson> findAll();
    Optional<Lesson> findByIdLessonId(int lessonId);
    Optional<Lesson> findByName(String name);
}
