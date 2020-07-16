package ua.com.foxminded.university.spring.service;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Timeslot;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
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
    void testGetDayTimetableForTeacher_ShouldThrowInvalidDataException_WhenLessonHasInvalidFieldValues() {
        int teacherId = 1;
        DayOfWeek day = DayOfWeek.MONDAY;      
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
        
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(0, 1, teacherId, 1, day, 1));
        lessons.add(new Lesson(1, 0, teacherId, 2, day, 2));
        lessons.add(new Lesson(2, 2, 0, 3, day, 3));
        lessons.add(new Lesson(3, 3, teacherId, 0, day, 4));
        lessons.add(new Lesson(4, 4, teacherId, 4, null, 5));
        lessons.add(new Lesson(5, 5, teacherId, 5, day, 0));
       
        for (Lesson lesson: lessons) {
            int lessonId = lesson.getId();
            int timeslotId = lesson.getTimeslotId();
            TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            notFormattedDayTimetable.clear();
            notFormattedDayTimetable.add(timeslotIdAndLessonId);  
            
            Mockito.reset(lessonDaoImpl);
            Mockito.when(lessonDaoImpl.getTeachersTimeslotIdAndLessonIdPairs(teacherId, day)).thenReturn(notFormattedDayTimetable);
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
            
            Assertions.assertThrows(InvalidDataException.class, () -> {
                service.getDayTimetableForTeacher(teacherId, day);
            });
        }
    }
   

    @Test
    void testGetDayTimetableForTeacher_ShouldReturnTimeslotToLessonMap_WhenRequesteWithTeacherIdAndDayOfWeek() throws DatabaseException, InvalidDataException {
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
    void testGetWeekTimetableForTeacher_ShouldReturnWeekTimetableForTeacher_WhenRequestedWithTeacherId() throws DatabaseException, InvalidDataException {
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
    void testGetDayTimetableForGroup_ShouldThrowInvalidDataException_WhenLessonHasInvalidFieldValues() {
        int groupId = 1;
        DayOfWeek day = DayOfWeek.MONDAY;      
        List<TimeslotIdLessonIdPair> notFormattedDayTimetable = new ArrayList<>();
        
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(0, 1, 1, groupId, day, 1));
        lessons.add(new Lesson(1, 0, 2, groupId, day, 2));
        lessons.add(new Lesson(2, 2, 0, groupId, day, 3));
        lessons.add(new Lesson(3, 3, 3, 0, day, 4));
        lessons.add(new Lesson(4, 4, 4, groupId, null, 5));
        lessons.add(new Lesson(5, 5, 5, groupId, day, 0));
       
        for (Lesson lesson: lessons) {
            int lessonId = lesson.getId();
            int timeslotId = lesson.getTimeslotId();
            TimeslotIdLessonIdPair timeslotIdAndLessonId = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            notFormattedDayTimetable.clear();
            notFormattedDayTimetable.add(timeslotIdAndLessonId);  
            
            Mockito.reset(lessonDaoImpl);
            Mockito.when(lessonDaoImpl.getGroupsTimeslotIdAndLessonIdPairs(groupId, day)).thenReturn(notFormattedDayTimetable);
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
            
            Assertions.assertThrows(InvalidDataException.class, () -> {
                service.getDayTimetableForGroup(groupId, day);
            });
        }
    }


    @Test
    void testGetDayTimetableForGroup_ShouldReturnTimeslotToLessonMap_WhenRequesteWithGroupIdAndDayOfWeek() throws DatabaseException, InvalidDataException {
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
    void testGetWeekTimetableForGroup_ShouldReturnWeekTimetableForGroup_WhenRequestedWithGroupId() throws DatabaseException, InvalidDataException {
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
