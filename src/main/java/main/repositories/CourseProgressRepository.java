package main.repositories;
import main.model.Course;
import main.model.CourseProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseProgressRepository extends CrudRepository<CourseProgress, Integer> {
    List<CourseProgress> findAll();
}
