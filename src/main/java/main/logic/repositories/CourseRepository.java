package main.logic.repositories;

import main.logic.entities.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course,Integer> {
    List<Course> findAll();
    List<Course> findByOrderByIdAsc();
    Optional<Course> findByName(String name);
    Optional<Course> findByCourseId(int courseId);
    Optional<Course> findByNameContaining(String name);
    Optional<Course> findByDescriptionContaining(String description);
}
