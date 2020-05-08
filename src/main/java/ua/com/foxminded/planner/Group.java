package ua.com.foxminded.planner;

public class Group {

    private int number;
    private Timetable timetable;
    
    @Override
    public String toString() { 
        return String.valueOf(number); 
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
