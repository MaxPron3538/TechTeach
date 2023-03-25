package main.logic.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;


@Entity
public class AnswerOption {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String possibleAnswer;

    @ManyToOne
    @JoinColumn(name = "test_id",nullable = false)
    private TestPoint testPoint;

    public int getId() {
        return id;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public void setTestPoint(TestPoint testPoint) {
        this.testPoint = testPoint;
    }

}

