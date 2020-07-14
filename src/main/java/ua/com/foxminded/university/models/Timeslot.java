package ua.com.foxminded.university.models;

import java.time.LocalTime;

public enum Timeslot {

    FIRST(1, LocalTime.of(9, 0), LocalTime.of(10, 35)), SECOND(2, LocalTime.of(10, 50), LocalTime.of(12, 25)),
    THIRD(3, LocalTime.of(13, 30), LocalTime.of(15, 05)), FOURTH(4, LocalTime.of(15, 20), LocalTime.of(16, 55)),
    FIFTH(5, LocalTime.of(17, 5), LocalTime.of(18, 40));

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    private static final String INVALID_ID_MESSAGE = "Invalid id. Valid id values: 1, 2, 3, 4, 5";

    private Timeslot(int id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static Timeslot getTimeslotById(int id) {
        Timeslot timeslot;
        switch (id) {
        case 1:
            timeslot = Timeslot.FIRST;
            break;
        case 2:
            timeslot = Timeslot.SECOND;
            break;
        case 3:
            timeslot = Timeslot.THIRD;
            break;
        case 4:
            timeslot = Timeslot.FOURTH;
            break;
        case 5:
            timeslot = Timeslot.FIFTH;
            break;
        default:
            throw new IllegalArgumentException(INVALID_ID_MESSAGE);
        }
        return timeslot;
    }

    @Override
    public String toString() {
        return "Timeslot [id=" + id + "]";
    }

}
