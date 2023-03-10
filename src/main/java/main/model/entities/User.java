package main.model.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String displayName;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    public Role role;

    @ManyToMany
    @JoinTable(
            name = "Registration",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;

    @OneToMany(mappedBy = "student")
    private List<CourseProgress> courseProgresses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public List<CourseProgress> getCourseProgresses() {
        return courseProgresses;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
