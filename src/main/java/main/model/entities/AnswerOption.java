package main.model.entities;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;


@Entity
public class AnswerOption {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "test_id",nullable = false)
    TestPoint testPoint;

    public int getId() {
        return id;
    }

    public void setTestPoint(TestPoint testPoint) {
        this.testPoint = testPoint;
    }

}

