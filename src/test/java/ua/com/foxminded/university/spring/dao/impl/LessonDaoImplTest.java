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
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Timeslot;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

class LessonDaoImplTest {
    
    private static final String SQL_GET_LESSON_BY_ID = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  teachers\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
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
    
    @Mock
    JdbcTemplate jdbcTemplate;
    LessonDao lessonDao = new LessonDaoImpl(jdbcTemplate);
    
    private LessonDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(lessonDao, "jdbcTemplate", jdbcTemplate);
    }
    
    @Test
    void testGetById_ShouldReturnLesson_WhenRequestedByLessonIdAsParam() {   
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
    void testGetAll_ShouldReturnListOfLessons_WhenRequestedWithNoParam() {
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
    void testDelete_ShouldDeleteOneLesson_WhenRequestedByLessonIdAsParam() {
        Lesson lesson = new Lesson();
        int lessonId = 0;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(1);
        assertTrue(lessonDao.delete(lesson));
    }
    
    @Test
    void testDelete_ShouldDeleteTwoLessons_WhenRequestedByLessonIdAsParam() {
        Lesson lesson = new Lesson();
        int lessonId = 1;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(2);
        assertTrue(lessonDao.delete(lesson));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroLessons_WhenRequestedByLessonIdAsParam() {
        Lesson lesson = new Lesson();
        int lessonId = 3;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_LESSON, lessonId)).thenReturn(0);
        assertFalse(lessonDao.delete(lesson));
    }

    @Test
    void testUpdate_ShouldReturnTrue_WhenRequestedByLessonAsParam() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
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
    void testUpdate_ShouldReturnFalse_WhenRequestedByLessonAsParam() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
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
    void testCreate_ShouldReturnTrue_WhenRequestedByLessonAsParam() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
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
    void testCreate_ShouldReturnFalse_WhenRequestedByLessonAsParam() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        int timeslotId = lesson.getTimeslotId();
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId)).thenReturn(0); 
        assertFalse(lessonDao.create(lesson));
    }

    void initialise(Lesson lesson) {
        lesson.setId(0); 
        lesson.setSubjectId(0); 
        lesson.setTeacherId(0);
        lesson.setGroupId(0);
        DayOfWeek day = DayOfWeek.MONDAY;
        lesson.setDay(day);         
        int timeslotId = Timeslot.FIRST.getId();
        lesson.setTimeslotId(timeslotId);
    }
}
