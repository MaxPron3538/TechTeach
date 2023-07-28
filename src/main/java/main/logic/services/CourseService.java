package main.logic.services;

import main.logic.entities.*;
import main.logic.repositories.CourseProgressRepository;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.RegistrationRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseProgressRepository courseProgressRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    public List<Course> getAllCourses(){
        return courseRepository.findByOrderByIdAsc();
    }

    public Optional<Course> addCourse(Course course) {
        Optional<Course> optionalCourse = courseRepository.findByName(course.getName());

        if(!optionalCourse.isPresent()){
            courseRepository.save(course);
            return Optional.of(course);
        }
        return Optional.empty();
    }

    public boolean openAccess(int id){
        Course course = courseRepository.findById(id).orElse(null);

        if(course != null) {
            course.setAvailable(true);
            return course.getAvailable();
        }
        return false;
    }

    public Optional<Course> updateCourse(Course course,int id){
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            Course updatedCourse = optionalCourse.get();
            updatedCourse.setName(course.getName());
            updatedCourse.setDescription(course.getDescription());
            courseRepository.save(updatedCourse);

            return Optional.of(updatedCourse);
        }
        return Optional.empty();
    }

    public Optional<Course> getCourseByContent(String content){
        Optional<Course> optionalCourse = courseRepository.findByNameContaining(content);

        if(!optionalCourse.isPresent()){
            return courseRepository.findByDescriptionContaining(content);
        }
        return optionalCourse;
    }

    public Optional<Course> getCourseById(int id){
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCourseId(int courseId){
        return courseRepository.findByCourseId(courseId);
    }

    public Optional<Course> deleteCourse(int id){
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent()){
            courseRepository.deleteById(id);
            return optionalCourse;
        }
        return Optional.empty();
    }

    public boolean submitStudentOnCourse(User user,Course course,int id){
        CourseProgress progress = courseProgressRepository.findAll().stream().filter(s -> s.getCourse().getId() == id).findAny().orElse(null);

        if (progress == null) {
            progress = new CourseProgress();
            progress.setCourse(course);
            progress.setStudent(user);
            progress.setSubscriptionType(SubscriptionType.user);
            progress.setSubscribedAt(LocalDate.now());
            progress.setLessonProgress(0);
            int month = LocalDate.now().getMonthValue() + (int) ((Math.random() * 12) - LocalDate.now().getMonth().getValue());
            int day = 1 + (int) (Math.random() * 28);
            progress.setValid_until(LocalDate.of(LocalDate.now().getYear(),
                    month, day));
            courseProgressRepository.save(progress);

            return true;
        }
        return false;
    }

    public boolean submitTeacherOnCourse(User user,Course course){
        Registration registration = registrationRepository.findAll().stream()
                .filter(s -> s.getTeacher().getId() == user.getId()
                        && s.getCourse().getId() == course.getId()).findAny().orElse(null);

        if (registration == null) {
            registration = new Registration();
            registration.setId(new RegistrationKey(user.getId(), course.getId()));
            registration.setTeacher(user);
            registration.setCourse(course);
            registrationRepository.save(registration);

            return true;
        }
        return false;
    }
}
