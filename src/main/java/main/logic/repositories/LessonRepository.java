package main.logic.repositories;

import main.logic.entities.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson,Integer> {
    List<Lesson> findAll();
    Lesson findByName(String name);
}
