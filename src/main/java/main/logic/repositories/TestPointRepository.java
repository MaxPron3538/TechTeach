package main.logic.repositories;

import main.logic.entities.TestPoint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestPointRepository extends CrudRepository<TestPoint,Integer> {
    List<TestPoint> findAll();
}



