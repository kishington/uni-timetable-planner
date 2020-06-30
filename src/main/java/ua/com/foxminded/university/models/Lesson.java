package ua.com.foxminded.university.models;

import java.time.DayOfWeek;

public class Lesson {
    private int id;
    private int subjectId;
    private int teacherId;
    private int groupId;
    private DayOfWeek day;
    private int timeslotId;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }
    
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public String toString() {
        return "Lesson [id=" + id + ", subjectId=" + subjectId + ", teacherId=" + teacherId + ", groupId=" + groupId
                + ", day=" + day + ", timeslotId=" + timeslotId + "]";
    }

}
