package main;

import main.model.Course;
import main.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {
    @Autowired
    CourseRepository repository;

    public void doRepository(){
        Course course = new Course();
        repository.save(course);
    }

    public static void main(String[] args){
        SpringApplication.run(Main.class,args);
    }
}
