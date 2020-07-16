package ua.com.foxminded.university.spring.service;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Timeslot;
import ua.com.foxminded.university.models.Timetable;
import ua.com.foxminded.university.spring.dao.impl.LessonDaoImpl;
import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;
import ua.com.foxminded.university.spring.service.exception.InvalidDataException;

@Service
public class TimetableService {
    
    private static final int INVALID_ID_VALUE = 0;
    
    private static final String INVALID_TIMESLOT_ID = "Invalid timeslotId.";
    private static final String INVALID_LESSON_ID = "Invalid lessonId.";
    private static final String INVALID_SUBJECT_ID = "Invalid subjectId for lessonId = ";
    private static final String INVALID_TEACHER_ID = "Invalid teachertId for lessonId = ";
    private static final String INVALID_GROUP_ID = "Invalid groupId for lessonId = ";
    private static final String INVALID_DAY_ID = "Day is null for lessonId = ";
    
    private LessonDaoImpl lessonDaoImpl;
    
    @Autowired
    public TimetableService(LessonDaoImpl lessonDaoImpl) {
        this.lessonDaoImpl = lessonDaoImpl;
    }
    
    public Map<Timeslot, Lesson> getDayTimetableForTeacher(int teacherId, DayOfWeek day) {
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = lessonDaoImpl.getTeachersTimeslotIdAndLessonIdPairs(teacherId, day);

        Map<Timeslot, Lesson> dayTimetable = new HashMap<>();
        for(int i = 0; i < notFormattedDayTimetable.size(); i++) {
            TimeslotIdLessonIdPair timeslotIdAndLessonId = notFormattedDayTimetable.get(i);
            int timeslotId = timeslotIdAndLessonId.getTimeslotId();
            int lessonId = timeslotIdAndLessonId.getLessonId();
            Timeslot timeslot = null;
            try {
                timeslot = Timeslot.getTimeslotById(timeslotId);
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException(INVALID_TIMESLOT_ID, e);
            }
            Lesson lesson = lessonDaoImpl.getById(lessonId);
            validate(lesson);
            dayTimetable.put(timeslot, lesson);
        }  
        return dayTimetable;
    } 

    public Timetable getWeekTimetableForTeacher(int teacherId) {
        Map<DayOfWeek, Map<Timeslot, Lesson>> timetableValue = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<Timeslot, Lesson> dayTimetable = getDayTimetableForTeacher(teacherId, day);
            timetableValue.put(day, dayTimetable);
        }
        Timetable timetable = new Timetable();
        timetable.setValue(timetableValue);
        return timetable;
    }

    public Map<Timeslot, Lesson> getDayTimetableForGroup(int groupId, DayOfWeek day) {
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = lessonDaoImpl.getGroupsTimeslotIdAndLessonIdPairs(groupId, day);
        Map<Timeslot, Lesson> dayTimetable = new HashMap<>();
        for(int i = 0; i < notFormattedDayTimetable.size(); i++) {
            TimeslotIdLessonIdPair timeslotIdAndLessonId = notFormattedDayTimetable.get(i);
            int timeslotId = timeslotIdAndLessonId.getTimeslotId();
            int lessonId = timeslotIdAndLessonId.getLessonId();
            
            Timeslot timeslot = null;
            try {
                timeslot = Timeslot.getTimeslotById(timeslotId);
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException(INVALID_TIMESLOT_ID, e);
            }
            Lesson lesson = lessonDaoImpl.getById(lessonId);
            validate(lesson);
            dayTimetable.put(timeslot, lesson);
        }  
        return dayTimetable;
    }
    
    public Timetable getWeekTimetableForGroup(int groupId) {
        Map<DayOfWeek, Map<Timeslot, Lesson>> timetableValue = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<Timeslot, Lesson> dayTimetable = getDayTimetableForGroup(groupId, day);
            timetableValue.put(day, dayTimetable);
        }
        Timetable timetable = new Timetable();
        timetable.setValue(timetableValue);
        return timetable;
    } 
    
    private void validate(Lesson lesson) {  
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teachertId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        DayOfWeek day = lesson.getDay();
        int timeslotId = lesson.getTimeslotId();
        
        int lowestTimeslotId = Timeslot.getLowestId();
        int highestTimeslotId = Timeslot.getHighestId();
        
        if(lessonId == INVALID_ID_VALUE) {
            throw new InvalidDataException(INVALID_LESSON_ID);
        }
        if(subjectId == INVALID_ID_VALUE) {
            throw new InvalidDataException(INVALID_SUBJECT_ID + lessonId);
        }
        if(teachertId == INVALID_ID_VALUE) {
            throw new InvalidDataException(INVALID_TEACHER_ID + lessonId);
        }
        if(groupId == INVALID_ID_VALUE) {
            throw new InvalidDataException(INVALID_GROUP_ID + lessonId);
        }
        if(day == null) {
            throw new InvalidDataException(INVALID_DAY_ID + lessonId);
        }
        if(timeslotId < lowestTimeslotId || timeslotId > highestTimeslotId) {
            throw new InvalidDataException(INVALID_TIMESLOT_ID);
        }
    }
}
