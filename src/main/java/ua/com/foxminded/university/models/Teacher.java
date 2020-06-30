package ua.com.foxminded.university.models;

import java.util.List;

public class Teacher {
    private int id;
    private String firstName;
    private String lastName;
    private List<Subject> subjects;
    private Timetable timetable;

    public Teacher() {
    }

    public Teacher(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
