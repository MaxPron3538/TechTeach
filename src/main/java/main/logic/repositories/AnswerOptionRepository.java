package main.logic.repositories;

import main.logic.entities.AnswerOption;
import main.logic.entities.TestPoint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerOptionRepository extends CrudRepository<AnswerOption,Integer> {
    List<AnswerOption> findAll();
}

