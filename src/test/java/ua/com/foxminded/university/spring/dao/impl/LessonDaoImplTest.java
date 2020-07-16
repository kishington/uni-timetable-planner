package ua.com.foxminded.university.spring.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import java.time.format.TextStyle;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.spring.dao.mappers.TimeslotIdLessonIdMapper;
import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

class LessonDaoImplTest {
    
    private static final String SQL_GET_LESSON_BY_ID = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  lesson_id = ?\n";
    private static final String SQL_GET_ALL = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  lessons\n";
    private static final String SQL_DELETE_LESSON = "" +
            "delete from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  lesson_id = ?\n";
    private static final String SQL_UPDATE_LESSON = "" +
            "update\n" + 
            "  lessons\n" + 
            "set\n" + 
            "  subject_id = ?,\n" + 
            "  teacher_id = ?,\n" + 
            "  group_id = ?,\n" + 
            "  day = ?,\n" + 
            "  timeslot_id = ?\n" + 
            "where\n" + 
            "  lesson_id = ?\n"; 
    private static final String SQL_INSERT_LESSON = "" +
            "insert into lessons(\n" + 
            "  lesson_id, subject_id, teacher_id,\n" + 
            "  group_id, day, timeslot_id\n" + 
            ")\n" + 
            "values\n" + 
            "  (?, ?, ?, ?, ?, ?)\n"; 
    private static final String SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER = "" + 
            "select\n" + 
            "  count(*)\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
    private static final String SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP = "" + 
            "select\n" + 
            "  count(*)\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n";
    private static final String SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY = "" +
            "select\n" + 
            "  lesson_id,\n" +
            "  subject_id,\n" + 
            "  teacher_id,\n" + 
            "  group_id,\n" + 
            "  day,\n" + 
            "  timeslot_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n" + 
            "  and upper(day) = ?\n";
    private static final String SQL_GET_TEACHER_DAY_TIMETABLE = "" +
            "select\n" + 
            "  timeslot_id,\n" + 
            "  lesson_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  teacher_id = ?\n" + 
            "  and upper(day) = ?\n";
    private static final String SQL_GET_GROUP_DAY_TIMETABLE = "" +
            "select\n" + 
            "  timeslot_id,\n" + 
            "  lesson_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n" + 
            "  and upper(day) = ?\n";
    
    @Mock
    JdbcTemplate jdbcTemplate;
    LessonDaoImpl lessonDao;
    
    private LessonDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        lessonDao = new LessonDaoImpl(jdbcTemplate);
    }
    
    @Test
    void testGetById_ShouldReturnLesson_WhenRequestedByLessonIdAsParam() throws DatabaseException {   
        for(int lessonId = 0; lessonId < 5; lessonId++) {
            Lesson lesson = new Lesson();
            lesson.setId(lessonId);
            Mockito.when(jdbcTemplate.queryForObject(eq(SQL_GET_LESSON_BY_ID), eq(new Object[] {lessonId}), any(LessonMapper.class))).thenReturn(lesson); 
        }
        for(int expectedId = 0; expectedId < 5; expectedId++) {
            Lesson actualLesson = lessonDao.getById(expectedId);
            int actualId = actualLesson.getId();
            assertEquals(expectedId, actualId);
        }  
    }

    @Test
    void testGetAll_ShouldReturnListOfLessons_WhenRequestedWithNoParam() throws DatabaseException {
        List<Lesson> lessons = new ArrayList<>();
        for(int lessonId = 0; lessonId < 5; lessonId++) {
            Lesson lesson = new Lesson();
            lesson.setId(lessonId);
            lessons.add(lesson);
        }
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_ALL), any(LessonMapper.class))).thenReturn(lessons);
        assertEquals(lessons, lessonDao.getAll());
    }

    @Test
    void testDelete_ShouldDeleteOneLesson_WhenRequestedByLessonIdAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        int lessonId = 0;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(1);
        assertTrue(lessonDao.delete(lesson));
    }
    
    @Test
    void testDelete_ShouldDeleteTwoLessons_WhenRequestedByLessonIdAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        int lessonId = 1;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(2);
        assertTrue(lessonDao.delete(lesson));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroLessons_WhenRequestedByLessonIdAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        int lessonId = 3;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(0);
        assertFalse(lessonDao.delete(lesson));
    }

    @Test
    void testUpdate_ShouldReturnTrue_WhenRequestedByLessonAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        initialise(lesson, 0, 0, 0, 0, DayOfWeek.MONDAY, 1);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);
        int timeslotId = lesson.getTimeslotId();
        
        Mockito.when(jdbcTemplate.update(SQL_UPDATE_LESSON, subjectId, teacherId, groupId, day, timeslotId, lessonId)).thenReturn(1);
        assertTrue(lessonDao.update(lesson));
    }
    
    @Test
    void testUpdate_ShouldReturnFalse_WhenRequestedByLessonAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        initialise(lesson, 0, 0, 0, 0, DayOfWeek.MONDAY, 1);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getGroupId();
        int groupId = lesson.getGroupId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);
        int timeslotId = lesson.getTimeslotId();
        
        String sqlUpdLesson = "UPDATE lessons SET subject_id = ?, teacher_id = ?, group_id = ?, day = ?, timeslot_id = ? WHERE lesson_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdLesson, subjectId, teacherId, groupId, day, timeslotId, lessonId)).thenReturn(0);
        assertFalse(lessonDao.update(lesson));
    }  

    @Test
    void testCreate_ShouldReturnTrue_WhenRequestedByLessonAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        initialise(lesson, 0, 0, 0, 0, DayOfWeek.MONDAY, 1);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        int timeslotId = lesson.getTimeslotId();
        
        String sqlInsertLesson = "" +
                "insert into lessons(\n" + 
                "  lesson_id, subject_id, teacher_id,\n" + 
                "  group_id, day, timeslot_id\n" + 
                ")\n" + 
                "values\n" + 
                "  (?, ?, ?, ?, ?, ?)\n"; 
        Mockito.when(jdbcTemplate.update(sqlInsertLesson, lessonId, subjectId, teacherId, groupId, day, timeslotId)).thenReturn(1); 
        assertTrue(lessonDao.create(lesson));
    }
    
    @Test
    void testCreate_ShouldReturnFalse_WhenRequestedByLessonAsParam() throws DatabaseException {
        Lesson lesson = new Lesson();
        initialise(lesson, 0, 0, 0, 0, DayOfWeek.MONDAY, 1);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        int timeslotId = lesson.getTimeslotId();
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId)).thenReturn(0); 
        assertFalse(lessonDao.create(lesson));
    }
    
    @Test
    void testGetNumberOfLessonsPerWeekForTeacher_ShouldReturnZero_WhenTeacherIdDosntExist() throws DatabaseException {
        int teacherId = 0;
        Mockito.when(jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER, new Object[] { teacherId }, Integer.class)).thenReturn(0);
        int expectedNumberOfLessons = 0;
        int actualNumberOfLessons = lessonDao.getNumberOfLessonsPerWeekForTeacher(teacherId);
        assertEquals(expectedNumberOfLessons, actualNumberOfLessons);
    }
    
    @Test
    void testGetNumberOfLessonsPerWeekForTeacher_ShouldReturnPositiveInt_WhenTeacherIdAndLessonsExist() throws DatabaseException {
        int teacherId = 0;
        Mockito.when(jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER, new Object[] { teacherId }, Integer.class)).thenReturn(4);
        int expectedNumberOfLessons = 4;
        int actualNumberOfLessons = lessonDao.getNumberOfLessonsPerWeekForTeacher(teacherId);
        assertEquals(expectedNumberOfLessons, actualNumberOfLessons);
    }
    
    @Test
    void testGetNumberOfLessonsPerWeekForGroup_ShouldReturnZero_WhenGroupIdDoesntExist() throws DatabaseException {
        int groupId = 0;
        Mockito.when(jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP, new Object[] { groupId }, Integer.class)).thenReturn(0);
        int expectedNumberOfLessons = 0;
        int actualNumberOfLessons = lessonDao.getNumberOfLessonsPerWeekForGroup(groupId);
        assertEquals(expectedNumberOfLessons, actualNumberOfLessons);
    }
    
    @Test
    void testGetNumberOfLessonsPerWeekForGroup_ShouldPositiveInt_WhenGroupIdAndLessonsExist() throws DatabaseException {
        int groupId = 0;
        Mockito.when(jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP, new Object[] { groupId }, Integer.class)).thenReturn(10);
        int expectedNumberOfLessons = 10;
        int actualNumberOfLessons = lessonDao.getNumberOfLessonsPerWeekForGroup(groupId);
        assertEquals(expectedNumberOfLessons, actualNumberOfLessons);
    }
    
    @Test
    void testGetAllLessonsForWeek_ShouldReturnListOfLessons_WhenRequestedByGroupIdAndDay() throws DatabaseException {
        int groupId = 1;
        String dayString = "THURSDAY";
        DayOfWeek day = DayOfWeek.valueOf(dayString);
        
        List<Lesson> lessons = new ArrayList<>();
        int subjectId = 0;
        int teacherId = 0;
        int timeslotId = 1;
        for(int lessonId = 0; lessonId < 5; lessonId++) {
            Lesson lesson = new Lesson();
            initialise(lesson, lessonId, subjectId, teacherId, groupId, day, timeslotId);  
            lessons.add(lesson);
            subjectId++;
            teacherId++;
            timeslotId++;
        }
        
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY), eq(new Object[] { groupId, dayString }), any(LessonMapper.class))).thenReturn(lessons);
        
        List<Lesson> actualLessons = lessonDao.getAllLessonsForDay(groupId, dayString);
        for (int lessonId = 0; lessonId < lessons.size(); lessonId++) {
            Lesson expectedLesson = lessons.get(lessonId);
            Lesson actualLesson = actualLessons.get(lessonId);
            assertEquals(expectedLesson, actualLesson);
        }
    }
    
    @Test
    void testGetTeachersTimeslotIdAndLessonIdPairs_ShouldReturnListOfTimeslotIdAndLessonIdPairs_WhenRequestedWithTeacherIdAndDayOfWeek() throws DatabaseException {
        int teacherId = 0;
        DayOfWeek day = DayOfWeek.MONDAY;
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        
        List<TimeslotIdLessonIdPair> expectedTimeslotIdAndLessonIdPairs = new ArrayList<>();
        int lessonId = 0;
        for (int timeslotId = 1; timeslotId <= 5; timeslotId++) {
            TimeslotIdLessonIdPair expectedTimeslotIdLessonIdPair = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            expectedTimeslotIdAndLessonIdPairs.add(expectedTimeslotIdLessonIdPair);
            lessonId++;
        }
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_TEACHER_DAY_TIMETABLE), eq(new Object[] { teacherId, dayString }), any(TimeslotIdLessonIdMapper.class))).thenReturn(expectedTimeslotIdAndLessonIdPairs);
        
        List<TimeslotIdLessonIdPair> actualTimeslotIdAndLessonIdPairs = lessonDao.getTeachersTimeslotIdAndLessonIdPairs(teacherId, day);
        
        for (int i = 0; i < expectedTimeslotIdAndLessonIdPairs.size(); i++) {
            TimeslotIdLessonIdPair expectedTimeslotIdLessonId = expectedTimeslotIdAndLessonIdPairs.get(i);
            TimeslotIdLessonIdPair actualTimeslotIdAndLessonId = actualTimeslotIdAndLessonIdPairs.get(i);
            assertEquals(expectedTimeslotIdLessonId, actualTimeslotIdAndLessonId);
        }    
    }
    
    @Test
    void testGetGroupsTimeslotIdAndLessonIdPairs_ShouldReturnListOfTimeslotIdAndLessonIdPairs_WhenRequestedWithGroupIdAndDayOfWeek() throws DatabaseException {
        int groupId = 0;
        DayOfWeek day = DayOfWeek.MONDAY;
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        
        List<TimeslotIdLessonIdPair> expectedTimeslotIdAndLessonIdPairs = new ArrayList<>();
        int lessonId = 0;
        for (int timeslotId = 1; timeslotId <= 5; timeslotId++) {
            TimeslotIdLessonIdPair expectedTimeslotIdLessonIdPair = new TimeslotIdLessonIdPair(timeslotId, lessonId);
            expectedTimeslotIdAndLessonIdPairs.add(expectedTimeslotIdLessonIdPair);
            lessonId++;
        }
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_GROUP_DAY_TIMETABLE), eq(new Object[] { groupId, dayString }), any(TimeslotIdLessonIdMapper.class))).thenReturn(expectedTimeslotIdAndLessonIdPairs);
        
        List<TimeslotIdLessonIdPair> actualTimeslotIdAndLessonIdPairs = lessonDao.getGroupsTimeslotIdAndLessonIdPairs(groupId, day);
        
        for (int i = 0; i < expectedTimeslotIdAndLessonIdPairs.size(); i++) {
            TimeslotIdLessonIdPair expectedTimeslotIdLessonId = expectedTimeslotIdAndLessonIdPairs.get(i);
            TimeslotIdLessonIdPair actualTimeslotIdAndLessonId = actualTimeslotIdAndLessonIdPairs.get(i);
            assertEquals(expectedTimeslotIdLessonId, actualTimeslotIdAndLessonId);
        }
    }

    void initialise(Lesson lesson, int lessonId, int subjectId, int teacherId, int groupId, DayOfWeek day, int timeslotId) {
        lesson.setId(lessonId); 
        lesson.setSubjectId(subjectId); 
        lesson.setTeacherId(teacherId);
        lesson.setGroupId(groupId);
        lesson.setDay(day);         
        lesson.setTimeslotId(timeslotId);
    }
}
