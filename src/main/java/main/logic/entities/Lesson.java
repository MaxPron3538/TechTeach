package main.logic.entities;

import javax.persistence.*;

@Entity
public class Lesson {

    @EmbeddedId
    private LessonKey lessonId;

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

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setLessonId(LessonKey lessonId) {
        this.lessonId = lessonId;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

