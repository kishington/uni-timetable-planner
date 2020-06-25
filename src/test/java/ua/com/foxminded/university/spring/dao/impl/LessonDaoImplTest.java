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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.planner.Group;
import ua.com.foxminded.university.planner.Lesson;
import ua.com.foxminded.university.planner.Subject;
import ua.com.foxminded.university.planner.Teacher;
import ua.com.foxminded.university.planner.Timeslot;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;



class LessonDaoImplTest {
    
    @Mock
    JdbcTemplate jdbcTemplate;
    LessonDao lessonDao = new LessonDaoImpl();
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(lessonDao, "jdbcTemplate", jdbcTemplate);
    }
    
    @Test
    void testGetById() {     
        for(int lessonId = 0; lessonId < 5; lessonId++) {
            Lesson lesson = new Lesson();
            lesson.setId(lessonId);
            String sqlSelectById = "SELECT * FROM lessons WHERE lesson_id = ?";
            Mockito.when(jdbcTemplate.queryForObject(eq(sqlSelectById), eq(new Object[] {lessonId}), any(LessonMapper.class))).thenReturn(lesson); 
        }
        for(int expectedId = 0; expectedId < 5; expectedId++) {
            Lesson actualLesson = lessonDao.getById(expectedId);
            int actualId = actualLesson.getId();
            assertEquals(expectedId, actualId);
        }  
    }

    @Test
    void testGetAll() {
        List<Lesson> lessons = new ArrayList<>();
        for(int lessonId = 0; lessonId < 5; lessonId++) {
            Lesson lesson = new Lesson();
            lesson.setId(lessonId);
            lessons.add(lesson);
        }
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM lessons"), any(LessonMapper.class))).thenReturn(lessons);
        assertEquals(lessons, lessonDao.getAll());
    }

    @Test
    void testDelete_deletedOneLesson() {
        Lesson lesson = new Lesson();
        int lessonId = 0;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update("DELETE FROM lessons WHERE lesson_id = ?", lessonId)).thenReturn(1);
        assertTrue(lessonDao.delete(lesson));
        Mockito.when(jdbcTemplate.update("DELETE FROM lessons WHERE lesson_id = ?", 2)).thenReturn(0);
    }
    
    @Test
    void testDelete_deletedTwoLessons() {
        Lesson lesson = new Lesson();
        int lessonId = 1;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update("DELETE FROM lessons WHERE lesson_id = ?", lessonId)).thenReturn(2);
        assertTrue(lessonDao.delete(lesson));
    }
    @Test
    void testDelete_deletedZeroLessons() {
        Lesson lesson = new Lesson();
        int lessonId = 3;
        lesson.setId(lessonId);
        Mockito.when(jdbcTemplate.update("DELETE FROM lessons WHERE lesson_id = ?", lessonId)).thenReturn(0);
        assertFalse(lessonDao.delete(lesson));
    }

    @Test
    void testUpdate_updateSuccess() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);
        int timeslotId = lesson.getTimeslot().getId();
        
        String sqlUpdLesson = "UPDATE lessons SET subject_id = ?, teacher_id = ?, group_id = ?, day = ?, timeslot_id = ? WHERE lesson_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdLesson, subjectId, teacherId, groupId, day, timeslotId, lessonId)).thenReturn(1);
        assertTrue(lessonDao.update(lesson));
    }
    
    @Test
    void testUpdate_updateFail() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);
        int timeslotId = lesson.getTimeslot().getId();
        
        String sqlUpdLesson = "UPDATE lessons SET subject_id = ?, teacher_id = ?, group_id = ?, day = ?, timeslot_id = ? WHERE lesson_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdLesson, subjectId, teacherId, groupId, day, timeslotId, lessonId)).thenReturn(0);
        assertFalse(lessonDao.update(lesson));
    }  

    @Test
    void testCreate_createSuccess() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        int timeslotId = lesson.getTimeslot().getId();
        
        String sqlInsertLesson = "INSERT INTO lessons(lesson_id, subject_id, teacher_id, group_id, day, timeslot_id) values(?,?,?,?,?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertLesson, lessonId, subjectId, teacherId, groupId, day, timeslotId)).thenReturn(1); 
        assertTrue(lessonDao.create(lesson));
    }
    
    @Test
    void testCreate_createFail() {
        Lesson lesson = new Lesson();
        initialise(lesson);
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        int timeslotId = lesson.getTimeslot().getId();
        
        String sqlInsertLesson = "INSERT INTO lessons(lesson_id, subject_id, teacher_id, group_id, day, timeslot_id) values(?,?,?,?,?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertLesson, lessonId, subjectId, teacherId, groupId, day, timeslotId)).thenReturn(0); 
        assertFalse(lessonDao.create(lesson));
    }

    void initialise(Lesson lesson) {
        lesson.setId(0); 
        
        Subject subject = new Subject();
        subject.setId(0);  
        lesson.setSubject(subject);
        
        Teacher teacher = new Teacher();
        teacher.setId(0);  
        lesson.setTeacher(teacher);
        
        Group group = new Group();
        group.setId(0);    
        lesson.setGroup(group);
        
        DayOfWeek day = DayOfWeek.MONDAY;
        lesson.setDay(day);  
        
        Timeslot timeslot = Timeslot.FIRST;
        lesson.setTimeslot(timeslot); 
    }
}
