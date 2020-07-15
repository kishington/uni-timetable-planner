package ua.com.foxminded.university.spring.dao.util;

public class TimeslotIdLessonIdPair {
    private int timeslotId;
    private int lessonId;
    
    public TimeslotIdLessonIdPair() {    
    }
    public TimeslotIdLessonIdPair(int timeslotId, int lessonId) {
        this.timeslotId = timeslotId;
        this.lessonId = lessonId;
    }
    
    public int getTimeslotId() {
        return timeslotId;
    }
    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }
    
    public int getLessonId() {
        return lessonId;
    }
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lessonId;
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
        TimeslotIdLessonIdPair other = (TimeslotIdLessonIdPair) obj;
        if (lessonId != other.lessonId)
            return false;
        if (timeslotId != other.timeslotId)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "TimeslotIdLessonIdPair [timeslotId=" + timeslotId + ", lessonId=" + lessonId + "]";
    }
    
}
