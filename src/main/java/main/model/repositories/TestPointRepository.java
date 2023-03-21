package main.model.repositories;

import main.model.entities.TestPoint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestPointRepository extends CrudRepository<TestPoint,Integer> {
    List<TestPoint> findAll();
}
