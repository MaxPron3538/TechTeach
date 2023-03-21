package main.model.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class TestPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;
    private List<String> answersOptions;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "lesson_id",nullable = false)
    private Lesson lesson;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswersOptions() {
        return answersOptions;
    }

    public void setAnswersOptions(List<String> answersOptions) {
        this.answersOptions = answersOptions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
