package planner;

import java.time.DayOfWeek;
import java.util.Map;

public class Timetable {
    private Map<DayOfWeek, Map<Timeslot, Lesson>> value;

    public void addLesson(DayOfWeek day, Timeslot timeslot, Lesson lesson) {

    }

    public void removeLesson(DayOfWeek day, Timeslot timeslot) {

    }

    public int getTotalWeeklyWorkload() {
        return 0;
    }

    public int getTotalDailyWorkload() {
        return 0;
    }

    public int getWeeklyWorkload(Subject subject) {
        return 0;
    }

    public int getDailyWorkload(Subject subject, DayOfWeek day) {
        return 0;
    }
}
