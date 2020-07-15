package ua.com.foxminded.university.spring.service;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Timeslot;
import ua.com.foxminded.university.spring.dao.impl.LessonDaoImpl;
import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;
import ua.com.foxminded.university.spring.service.exception.InvalidDataException;

class TimetableServiceTest {
    
    @Mock
    private LessonDaoImpl lessonDaoImpl;
    private TimetableService service;
    
    private TimetableServiceTest() {
        MockitoAnnotations.initMocks(this);
        service = new TimetableService(lessonDaoImpl);
    }
    
    @Test
    void testGetDayTimetableForTeacher_ShouldReturnTimeslotToLessonMap_WhenRequesteWithTeacherIdAndDayOfWeek() throws InvalidDataException {
        int teacherId = 1;
        DayOfWeek day = DayOfWeek.MONDAY;
        
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
        Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();
        
        Timeslot[] timeslots = Timeslot.values();
        int lessonId = 1;
        int subjectId = 1;
        int groupId = 1;
        for(Timeslot timeslot: timeslots) {
            int timeslotId = timeslot.getId();
            
            Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
            expectedDayTimetable.put(timeslot, lesson);
            
            TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            notFormattedDayTimetable.add(timeslotIdAndLessonId);
            
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);         
            lessonId++;
            subjectId++;
            groupId++;
        }
        
        Mockito.when(lessonDaoImpl.getTeachersTimeslotIdAndLessonIdPairs(teacherId, day)).thenReturn(notFormattedDayTimetable);
        
        Map<Timeslot, Lesson> actualDayTimetable = service.getDayTimetableForTeacher(teacherId, day);
        for(Timeslot timeslot: expectedDayTimetable.keySet()) {
            Lesson expectedLesson = expectedDayTimetable.get(timeslot);
            Lesson actualLesson = actualDayTimetable.get(timeslot);
            assertEquals(expectedLesson, actualLesson);     
        }
    }
   
    @Test
    void testGetWeekTimetableForTeacher_ShouldReturnWeekTimetableForTeacher_WhenRequestedWithTeacherId() throws InvalidDataException {
        int teacherId = 1;
        Map<DayOfWeek, Map<Timeslot, Lesson>> expectedWeekTimetable = new HashMap<>();

        int lessonId = 1;
        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            if (day != DayOfWeek.SUNDAY) {
                List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
                Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();

                Timeslot[] timeslots = Timeslot.values();
                int subjectId = 1;
                int groupId = 1;
                for (Timeslot timeslot : timeslots) {
                    int timeslotId = timeslot.getId();

                    Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
                    expectedDayTimetable.put(timeslot, lesson);

                    TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
                    notFormattedDayTimetable.add(timeslotIdAndLessonId);

                    Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
                    lessonId++;
                    subjectId++;
                    groupId++;
                }
                Mockito.when(lessonDaoImpl.getTeachersTimeslotIdAndLessonIdPairs(teacherId, day))
                        .thenReturn(notFormattedDayTimetable);

                expectedWeekTimetable.put(day, expectedDayTimetable);
            }
        }

        Map<DayOfWeek, Map<Timeslot, Lesson>> actualWeekTimetable = service.getWeekTimetableForTeacher(teacherId).getValue();
        expectedWeekTimetable.forEach((day, expectedDayTimetable) -> {
            Map<Timeslot, Lesson> actualDayTimetable = actualWeekTimetable.get(day);
            expectedDayTimetable.forEach((timeslot, Lesson) -> {
                Lesson expectedLesson = expectedDayTimetable.get(timeslot);
                Lesson actualLesson = actualDayTimetable.get(timeslot);
                assertEquals(expectedLesson, actualLesson);
            });
        });
    }

    @Test
    void testGetDayTimetableForGroup() throws InvalidDataException {
        int groupId = 1;
        DayOfWeek day = DayOfWeek.MONDAY;
        
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
        Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();
        
        Timeslot[] timeslots = Timeslot.values();
        int lessonId = 1;
        int subjectId = 1;
        int teacherId = 1;
        for(Timeslot timeslot: timeslots) {
            int timeslotId = timeslot.getId();
            
            Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
            expectedDayTimetable.put(timeslot, lesson);
            
            TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            notFormattedDayTimetable.add(timeslotIdAndLessonId);
            
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);         
            lessonId++;
            subjectId++;
            teacherId++;
        }
        
        Mockito.when(lessonDaoImpl.getGroupsTimeslotIdAndLessonIdPairs(groupId, day)).thenReturn(notFormattedDayTimetable);
        
        Map<Timeslot, Lesson> actualDayTimetable = service.getDayTimetableForGroup(groupId, day);
        for(Timeslot timeslot: expectedDayTimetable.keySet()) {
            Lesson expectedLesson = expectedDayTimetable.get(timeslot);
            Lesson actualLesson = actualDayTimetable.get(timeslot);
            assertEquals(expectedLesson, actualLesson);     
        }
    }

    @Test
    void testGetWeekTimetableForGroup_ShouldReturnWeekTimetableForGroup_WhenRequestedWithGroupId() throws InvalidDataException {
        int groupId = 1;
        Map<DayOfWeek, Map<Timeslot, Lesson>> expectedWeekTimetable = new HashMap<>();

        int lessonId = 1;
        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            if (day != DayOfWeek.SUNDAY) {
                List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
                Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();

                Timeslot[] timeslots = Timeslot.values();
                int subjectId = 1;
                int teacherId = 1;
                for (Timeslot timeslot : timeslots) {
                    int timeslotId = timeslot.getId();

                    Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
                    expectedDayTimetable.put(timeslot, lesson);

                    TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
                    notFormattedDayTimetable.add(timeslotIdAndLessonId);

                    Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
                    lessonId++;
                    subjectId++;
                    teacherId++;
                }
                Mockito.when(lessonDaoImpl.getGroupsTimeslotIdAndLessonIdPairs(groupId, day))
                        .thenReturn(notFormattedDayTimetable);

                expectedWeekTimetable.put(day, expectedDayTimetable);
            }
        }

        Map<DayOfWeek, Map<Timeslot, Lesson>> actualWeekTimetable = service.getWeekTimetableForGroup(groupId).getValue();
        expectedWeekTimetable.forEach((day, expectedDayTimetable) -> {
            Map<Timeslot, Lesson> actualDayTimetable = actualWeekTimetable.get(day);
            expectedDayTimetable.forEach((timeslot, Lesson) -> {
                Lesson expectedLesson = expectedDayTimetable.get(timeslot);
                Lesson actualLesson = actualDayTimetable.get(timeslot);
                assertEquals(expectedLesson, actualLesson);
            });
        });
    }

}
