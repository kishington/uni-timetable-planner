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

class TimetableServiceTest {
    
    @Mock
    private LessonDaoImpl lessonDaoImpl;
    private TimetableService service;
    
    private TimetableServiceTest() {
        MockitoAnnotations.initMocks(this);
        service = new TimetableService(lessonDaoImpl);
    }
    
    @Test
    void testGetDayTimetableForTeacher_ShouldReturnTimeslotToLessonMap_WhenRequesteWithTeacherIdAndDayOfWeek() {
        int teacherId = 0;
        DayOfWeek day = DayOfWeek.MONDAY;
        
        List<int[]> notFormattedDayTimetable = new ArrayList<>();
        Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();
        
        Timeslot[] timeslots = Timeslot.values();
        int lessonId = 0;
        int subjectId = 0;
        int groupId = 0;
        for(Timeslot timeslot: timeslots) {
            int timeslotId = timeslot.getId();
            
            Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
            expectedDayTimetable.put(timeslot, lesson);
            
            int[] timeslotIdAndLessonId = new int[] {timeslotId, lessonId};
            notFormattedDayTimetable.add(timeslotIdAndLessonId);
            
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);         
            lessonId++;
            subjectId++;
            groupId++;
        }
        
        Mockito.when(lessonDaoImpl.getTimeslotIdAndLessonIdPairsForTeacherForDay(teacherId, day)).thenReturn(notFormattedDayTimetable);
        
        Map<Timeslot, Lesson> actualDayTimetable = service.getDayTimetableForTeacher(teacherId, day);
        for(Timeslot timeslot: expectedDayTimetable.keySet()) {
            Lesson expectedLesson = expectedDayTimetable.get(timeslot);
            Lesson actualLesson = actualDayTimetable.get(timeslot);
            assertEquals(expectedLesson, actualLesson);     
        }
    }

    @Test
    void testGetWeekTimetableForTeacher_ShouldReturnWeekTimetableForTeacher_WhenRequestedWithTeacherId() {
        int teacherId = 0;
        Map<DayOfWeek, Map<Timeslot, Lesson>> expectedWeekTimetable = new HashMap<>();

        int lessonId = 0;
        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            if (day != DayOfWeek.SUNDAY) {
                List<int[]> notFormattedDayTimetable = new ArrayList<>();
                Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();

                Timeslot[] timeslots = Timeslot.values();
                int subjectId = 0;
                int groupId = 0;
                for (Timeslot timeslot : timeslots) {
                    int timeslotId = timeslot.getId();

                    Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
                    expectedDayTimetable.put(timeslot, lesson);

                    int[] timeslotIdAndLessonId = new int[] { timeslotId, lessonId };
                    notFormattedDayTimetable.add(timeslotIdAndLessonId);

                    Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
                    lessonId++;
                    subjectId++;
                    groupId++;
                }
                Mockito.when(lessonDaoImpl.getTimeslotIdAndLessonIdPairsForTeacherForDay(teacherId, day))
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
    void testGetDayTimetableForGroup() {
        int groupId = 0;
        DayOfWeek day = DayOfWeek.MONDAY;
        
        List<int[]> notFormattedDayTimetable = new ArrayList<>();
        Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();
        
        Timeslot[] timeslots = Timeslot.values();
        int lessonId = 0;
        int subjectId = 0;
        int teacherId = 0;
        for(Timeslot timeslot: timeslots) {
            int timeslotId = timeslot.getId();
            
            Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
            expectedDayTimetable.put(timeslot, lesson);
            
            int[] timeslotIdAndLessonId = new int[] {timeslotId, lessonId};
            notFormattedDayTimetable.add(timeslotIdAndLessonId);
            
            Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);         
            lessonId++;
            subjectId++;
            teacherId++;
        }
        
        Mockito.when(lessonDaoImpl.getTimeslotIdAndLessonIdPairsForGroupForDay(groupId, day)).thenReturn(notFormattedDayTimetable);
        
        Map<Timeslot, Lesson> actualDayTimetable = service.getDayTimetableForGroup(groupId, day);
        for(Timeslot timeslot: expectedDayTimetable.keySet()) {
            Lesson expectedLesson = expectedDayTimetable.get(timeslot);
            Lesson actualLesson = actualDayTimetable.get(timeslot);
            assertEquals(expectedLesson, actualLesson);     
        }
    }

    @Test
    void testGetWeekTimetableForGroup_ShouldReturnWeekTimetableForGroup_WhenRequestedWithGroupId() {
        int groupId = 0;
        Map<DayOfWeek, Map<Timeslot, Lesson>> expectedWeekTimetable = new HashMap<>();

        int lessonId = 0;
        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            if (day != DayOfWeek.SUNDAY) {
                List<int[]> notFormattedDayTimetable = new ArrayList<>();
                Map<Timeslot, Lesson> expectedDayTimetable = new HashMap<>();

                Timeslot[] timeslots = Timeslot.values();
                int subjectId = 0;
                int teacherId = 0;
                for (Timeslot timeslot : timeslots) {
                    int timeslotId = timeslot.getId();

                    Lesson lesson = new Lesson(lessonId, subjectId, teacherId, groupId, day, timeslotId);
                    expectedDayTimetable.put(timeslot, lesson);

                    int[] timeslotIdAndLessonId = new int[] { timeslotId, lessonId };
                    notFormattedDayTimetable.add(timeslotIdAndLessonId);

                    Mockito.when(lessonDaoImpl.getById(lessonId)).thenReturn(lesson);
                    lessonId++;
                    subjectId++;
                    teacherId++;
                }
                Mockito.when(lessonDaoImpl.getTimeslotIdAndLessonIdPairsForGroupForDay(groupId, day))
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
