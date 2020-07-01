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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + groupId;
        result = prime * result + id;
        result = prime * result + subjectId;
        result = prime * result + teacherId;
        result = prime * result + timeslotId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lesson other = (Lesson) obj;
        if (day != other.day)
            return false;
        if (groupId != other.groupId)
            return false;
        if (id != other.id)
            return false;
        if (subjectId != other.subjectId)
            return false;
        if (teacherId != other.teacherId)
            return false;
        if (timeslotId != other.timeslotId)
            return false;
        return true;
    }

}
