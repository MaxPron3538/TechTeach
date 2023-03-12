package main.repositories;
import main.model.Course;
import java.util.List;

public interface CourseProgressRepository {
    List<Course> findAll();
}
