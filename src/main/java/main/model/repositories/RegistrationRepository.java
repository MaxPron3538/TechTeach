package main.model.repositories;

import main.model.entities.Lesson;
import main.model.entities.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration,Integer> {
    List<Registration> findAll();
}
