package main.model;

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

}
