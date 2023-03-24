package main.logic.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RegistrationKey implements Serializable {
    @Column(name = "teacher_id")
    int teacherId;
    @Column(name = "course_id")
    int courseId;

    public RegistrationKey(){}

    public RegistrationKey(int teacherId,int courseId){
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationKey that = (RegistrationKey) o;
        return teacherId == that.teacherId && courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, courseId);
    }
}