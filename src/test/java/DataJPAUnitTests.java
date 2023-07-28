import com.fasterxml.jackson.databind.ObjectMapper;
import main.Main;
import main.controller.AuthorizationRestController;
import main.controller.CourseRestController;
import main.logic.entities.*;
import main.logic.models.UserModel;
import main.logic.repositories.CourseRepository;
import main.logic.repositories.LessonRepository;
import main.logic.repositories.UserRepository;
import main.logic.services.CourseService;
import main.logic.services.JwtUserDetailsService;
import main.logic.services.LessonService;
import org.checkerframework.checker.units.qual.C;
import org.hamcrest.CoreMatchers;
import org.hibernate.boot.model.source.spi.NaturalIdMutability;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;

@RunWith(MockitoJUnitRunner.class)
public class DataJPAUnitTests {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    Course record1 = new Course(1,1,"Java developer","This course for java developer","url");

    @Test
    public void addCourseTest() {
        Optional<Course> optionalCourse = courseService.addCourse(record1);
        Mockito.verify(courseRepository,Mockito.times(1)).save(record1);
        assertEquals(optionalCourse.get().getId(),record1.getId());
    }

    @Test
    public void updateCourseTest() {
        when(courseService.updateCourse(record1,1)).thenReturn(Optional.of(record1));
        Optional<Course> optionalCourse = courseService.updateCourse(record1,1);
        Mockito.verify(courseRepository,Mockito.times(1)).save(record1);
        assertEquals(optionalCourse.get().getId(),record1.getId());
    }

    @Test
    public void getCoursesTest() {
        when(courseService.getAllCourses()).thenReturn(ImmutableList.of());
        List<Course> savedCourses = courseService.getAllCourses();
        Mockito.verify(courseRepository).findByOrderByIdAsc();
    }

    @Test
    public void getCourseByIdTest() {
        when(courseService.getCourseById(record1.getId())).thenReturn(java.util.Optional.of(record1));
        Optional<Course> optionalCourse = courseService.getCourseById(1);
        assertEquals("Java developer",optionalCourse.get().getName());
    }

    @Test
    public void getCourseByContentTest() {
        when(courseService.getCourseByContent("Java")).thenReturn(java.util.Optional.of(record1));
        Optional<Course> optionalCourse = courseService.getCourseByContent("Java");
        assertEquals("Java developer",optionalCourse.get().getName());
    }

    @Test
    public void deleteCourseById(){
        when(courseService.deleteCourse(record1.getId())).thenReturn(java.util.Optional.of(record1));
        Optional<Course> optionalCourse = courseService.deleteCourse(1);
        assertEquals(optionalCourse.get().getId(),record1.getId());
    }

}


