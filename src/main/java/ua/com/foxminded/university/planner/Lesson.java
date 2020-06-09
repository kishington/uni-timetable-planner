package ua.com.foxminded.university.planner;

import java.time.DayOfWeek;

public class Lesson {
    private int id;
    private Subject subject;
    private Teacher teacher;
    private Group group;
    private DayOfWeek day;
    private Timeslot timeslot;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "Lesson [subject=" + subject + ", teacher=" + teacher + ", group=" + group + ", day=" + day
                + ", timeslot=" + timeslot + "]";
    }
}
