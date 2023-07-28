package main.logic.entities;


import javax.persistence.*;
import java.util.List;


@Entity
public class Course {

    public Course(){}

    public Course(int id,int courseId,String name,String description,String coverUrl){
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.coverUrl = coverUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "course_id")
    private  int courseId;
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    private String coverUrl;

    private boolean isAvailable = true;

    @ManyToMany(mappedBy = "courses")
    List<User> teachers;

    @OneToMany(mappedBy = "course")
    List<CourseProgress> courseProgresses;

    @OneToMany(mappedBy = "course")
    List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    List<TestPoint> tests;

    public int getId() {
        return id;
    }

    public int getCourse_id() {
        return courseId;
    }

    public void setCourse_id(int courseId) {
        this.courseId = courseId;
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

    public List<Lesson> getLessons(){
        return lessons;
    }

    public List<TestPoint> getTests() {
        return tests;
    }


    public void setAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public boolean getAvailable(){
        return isAvailable;
    }


}
