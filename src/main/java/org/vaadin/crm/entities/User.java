package org.vaadin.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String userName;
    private int age;
    private LocalDateTime datetime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Facility> facilities;

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="student_course",
            joinColumns=  @JoinColumn(name="course_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="student_id", referencedColumnName="id") )
    private Set<Student> student = new HashSet<>();
    */

    public User(String name, int age, LocalDateTime datetime) {
        this.userName = name;
        this.age = age;
        this.datetime = datetime;
    }
}
