package com.kirilldergunov.server.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "position")
    private String position;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "employee")
    private List<Meeting> meetings = new ArrayList<>();

    public Employee() {
    }

    public Employee(String fio, String position, String email, String phone) {
        this.fio = fio;
        this.position = position;
        this.email = email;
        this.phone = phone;
    }

    public void addMeetToEmployee(Meeting meeting) {
        if(meetings == null) {
            meetings = new ArrayList<>();
        }
        meetings.add(meeting);
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String name) {
        this.fio = fio;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "fio='" + fio + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}