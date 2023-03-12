package main.model;


import javax.persistence.*;
import java.util.List;


@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private  int course_id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "course")
    List<Registration> users;

    @OneToMany(mappedBy = "course")
    List<CourseProgress> courseProgresses;

    @OneToMany(mappedBy = "course")
    List<Lesson> lessons;

    private int lessonProgress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLessonProgress() {
        return lessonProgress;
    }

    public void setLessonProgress(int lessonProgress) {
        this.lessonProgress = lessonProgress;
    }
}
