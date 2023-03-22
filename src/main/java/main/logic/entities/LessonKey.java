package main.logic.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class LessonKey implements Serializable {

    @Column(name = "id",nullable = false)
    int id;

    @Column(name = "course_id",nullable = false)
    int courseId;

    LessonKey(){}

    public LessonKey(int id,int courseId){
        this.id = id;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonKey that = (LessonKey) o;
        return id == that.id && courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,courseId);
    }

}
