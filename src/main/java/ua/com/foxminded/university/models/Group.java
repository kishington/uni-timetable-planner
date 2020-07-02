package ua.com.foxminded.university.models;

public class Group {

    private int id;
    private Timetable timetable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
