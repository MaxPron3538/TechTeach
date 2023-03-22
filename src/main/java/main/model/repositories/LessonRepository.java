package main.model.repositories;

import main.model.entities.Lesson;
import main.model.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson,Integer> {
    List<Lesson> findAll();
    Lesson findByName(String name);
}
