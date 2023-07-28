package main.logic.entities;

import javax.persistence.*;

@Entity
public class Lesson {

    public Lesson(){}

    public Lesson(LessonKey id,String name,String description,String videoUrl){
        this.id = id;
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    @EmbeddedId
    private LessonKey id;

    @ManyToOne
    @MapsId("courseId")
    private Course course;

    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    private String videoUrl;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public LessonKey getId() {
        return id;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setLessonId(LessonKey id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

