package main.logic.entities;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class CourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id",nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;

    private LocalDate subscribedAt;
    private LocalDate valid_until;
    private SubscriptionType subscriptionType;
    private int lessonProgress;

    public Integer getId() {
        return id;
    }

    public void setStudent(User student) { this.student = student;}

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(LocalDate subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public LocalDate getValid_until() {
        return valid_until;
    }

    public void setValid_until(LocalDate valid_until) {
        this.valid_until = valid_until;
    }

    public Enum getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public int getLessonProgress() { return lessonProgress; }

    public void setLessonProgress(int lessonProgress) { this.lessonProgress = lessonProgress;}
}
