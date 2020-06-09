package ua.com.foxminded.university.planner;

public class Group {

    private int id;
    private Timetable timetable;

    public int getId() {
        return id;
    }

    public void setId(int number) {
        this.id = number;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return "Group [number=" + id + "]";
    }

}
