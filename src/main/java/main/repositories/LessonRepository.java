package main.repositories;

import main.model.Lesson;
import main.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson,Integer> {
    List<Lesson> findAll();
}
