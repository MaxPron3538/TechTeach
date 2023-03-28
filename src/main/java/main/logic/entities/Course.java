package main.logic.entities;


import javax.persistence.*;
import java.util.List;


@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private  int course_id;
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    private String coverUrl;

    @ManyToMany(mappedBy = "courses")
    List<User> teachers;

    @OneToMany(mappedBy = "course")
    List<CourseProgress> courseProgresses;

    @OneToMany(mappedBy = "course")
    List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    List<TestPoint> tests;

    private int lessonProgress;

    public int getId() {
        return id;
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

    public String getCoverUrl() { return coverUrl; }

    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public int getLessonProgress() {
        return lessonProgress;
    }

    public void setLessonProgress(int lessonProgress) {
        this.lessonProgress = lessonProgress;
    }

    public List<Lesson> getLessons(){
        return lessons;
    }

    public List<TestPoint> getTests() {
        return tests;
    }
}
