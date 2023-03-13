package main.model.repositories;
import main.model.entities.CourseProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseProgressRepository extends CrudRepository<CourseProgress, Integer> {
    List<CourseProgress> findAll();
}
