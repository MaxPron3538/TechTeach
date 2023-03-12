package main.repositories;

import main.model.Lesson;
import main.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson,Integer> {
    List<Lesson> findAll();
}
