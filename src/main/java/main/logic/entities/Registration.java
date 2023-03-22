package main.logic.entities;

import javax.persistence.*;

@Entity
public class Registration {

    @EmbeddedId
    RegistrationKey id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    Course course;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    User teacher;

    public void setId(RegistrationKey id) {
        this.id = id;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getTeacher() {
        return teacher;
    }

    public Course getCourse() {
        return course;
    }
}