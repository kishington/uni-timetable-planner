package ua.com.foxminded.university.models;

import java.time.LocalTime;

public enum Timeslot {

    FIRST(1, LocalTime.of(9, 0), LocalTime.of(10, 35)), 
    SECOND(2, LocalTime.of(10, 50), LocalTime.of(12, 25)),
    THIRD(3, LocalTime.of(13, 30), LocalTime.of(15, 05)), 
    FOURTH(4, LocalTime.of(15, 20), LocalTime.of(16, 55)),
    FIFTH(5, LocalTime.of(17, 5), LocalTime.of(18, 40));

    private static final int LOWEST_ID = 1;
    private static final int HIGHEST_ID = 5;
    
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    private static final String INVALID_ID_MESSAGE = " is an invalid timeslotId. Valid values are: 1, 2, 3, 4, 5";

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

    public static Timeslot getTimeslotById(int timeslotId) {
        Timeslot[] timeslots = Timeslot.values();
        for (Timeslot timeslot : timeslots) {
            int validTimeslotId = timeslot.getId();
            if (timeslotId == validTimeslotId) {
                return timeslot;
            }
        }
        throw new IllegalArgumentException(timeslotId + INVALID_ID_MESSAGE);
    }
    
    public static int getLowestId() {
        return LOWEST_ID;
    }
    
    public static int getHighestId() {
        return HIGHEST_ID;
    }

    @Override
    public String toString() {
        return "Timeslot [id=" + id + "]";
    }

}
