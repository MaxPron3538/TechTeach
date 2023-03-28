package main.logic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
public class TestPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(columnDefinition="TEXT")
    private String question;
    @Lob
    @Column(columnDefinition="TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;

    @OneToMany(mappedBy = "testPoint")
    List<AnswerOption> answerOptions;

    public int getId() { return id; }

    public String getQuestion() { return question; }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

