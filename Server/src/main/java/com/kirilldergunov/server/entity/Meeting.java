package com.kirilldergunov.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "status")
    private Status status;

    @Column(name = "place")
    private String place;

    @Column(name = "comment")
    private String comment;

    public Meeting() {
        this.status = Status.SCHEDULED;
    }

    public Meeting(LocalDateTime datetime, Status status, String place, String comment) {
        this.datetime = datetime;
        this.status = Status.SCHEDULED;
        this.place = place;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void addClientToMeeting(Client client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void addEmployeeToMeeting(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", client=" + client +
                ", employee=" + employee +
                ", datetime=" + datetime +
                ", status=" + status +
                ", place='" + place + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}