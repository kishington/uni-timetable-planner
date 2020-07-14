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

@Service
public class TimetableService {
    
    LessonDaoImpl lessonDaoImpl;
    
    @Autowired
    public TimetableService(LessonDaoImpl lessonDaoImpl) {
        this.lessonDaoImpl = lessonDaoImpl;
    }
    
    public Map<Timeslot, Lesson> getDayTimetableForTeacher(int teacherId, DayOfWeek day) {
        List<int[]> notFormattedDayTimetable = lessonDaoImpl.getTimeslotIdAndLessonIdPairsForTeacherForDay(teacherId, day);

        Map<Timeslot, Lesson> dayTimetable = new HashMap<>();
        for(int i = 0; i < notFormattedDayTimetable.size(); i++) {
            int[] timeslotIdAndLessonId = notFormattedDayTimetable.get(i);
            int timeslotId = timeslotIdAndLessonId[0];
            int lessonId = timeslotIdAndLessonId[1];
            
            Timeslot timeslot = Timeslot.getTimeslotById(timeslotId);
            Lesson lesson = lessonDaoImpl.getById(lessonId);
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
        List<int[]> notFormattedDayTimetable = lessonDaoImpl.getTimeslotIdAndLessonIdPairsForGroupForDay(groupId, day);
        Map<Timeslot, Lesson> dayTimetable = new HashMap<>();
        for(int i = 0; i < notFormattedDayTimetable.size(); i++) {
            int[] timeslotIdAndLessonId = notFormattedDayTimetable.get(i);
            int timeslotId = timeslotIdAndLessonId[0];
            int lessonId = timeslotIdAndLessonId[1];
            
            Timeslot timeslot = Timeslot.getTimeslotById(timeslotId);
            Lesson lesson = lessonDaoImpl.getById(lessonId);
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
}
