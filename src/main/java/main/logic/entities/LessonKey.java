package main.logic.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class LessonKey implements Serializable {

    @Column(name = "id",nullable = false)
    int lessonId;

    @Column(name = "course_id",nullable = false)
    int courseId;

    LessonKey(){}

    public LessonKey(int lessonId,int courseId){
        this.lessonId = lessonId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonKey that = (LessonKey) o;
        return lessonId == that.lessonId && courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId,courseId);
    }

    public int getId() {
        return lessonId;
    }

}
