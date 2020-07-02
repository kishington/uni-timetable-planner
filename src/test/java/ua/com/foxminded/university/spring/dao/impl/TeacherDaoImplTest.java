package ua.com.foxminded.university.spring.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

class TeacherDaoImplTest {
    
    private static final String SQL_GET_TEACHER_BY_ID = "" + 
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
            "  teachers\n";
    private static final String SQL_DELETE_TEACHER = "" +
            "delete from\n" + 
            "  teachers\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
    private static final String SQL_UPDATE_TEACHER = "" +
            "update\n" + 
            "  teachers\n" + 
            "set\n" + 
            "  first_name = ?,\n" + 
            "  last_name = ?\n" + 
            "where\n" + 
            "  teacher_id = ?\n"; 
    private static final String SQL_INSERT_TEACHER = "" +
            "insert into teachers(" + 
            "  teacher_id, first_name, last_name\n" + 
            ")\n" + 
            "values\n" + 
            "  (?, ?, ?)\n";
    
    @Mock
    JdbcTemplate jdbcTemplate;
    TeacherDaoImpl teacherDao;
    
    private TeacherDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        teacherDao = new TeacherDaoImpl(jdbcTemplate);
    }

    @Test
    void testGetById_ShouldReturnteacher_WhenRequestedByTeacherIdAsParam() {  
        for(int teacherId = 0; teacherId < 5; teacherId++) {
            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            Mockito.when(jdbcTemplate.queryForObject(eq(SQL_GET_TEACHER_BY_ID), eq(new Object[] {teacherId}), any(TeacherMapper.class))).thenReturn(teacher); 
        }
        for(int expectedId = 0; expectedId < 5; expectedId++) {
            Teacher actualTeacher = teacherDao.getById(expectedId);
            int actualId = actualTeacher.getId();
            assertEquals(expectedId, actualId);
        }  
    }
    
    @Test
    void testGetAll_ShouldReturnListOfTeachers_WhenRequestedWithNoParam() {
        List<Teacher> teachers = new ArrayList<>();
        for(int teacherId = 0; teacherId < 5; teacherId++) {
            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            teachers.add(teacher);
        }
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_ALL), any(TeacherMapper.class))).thenReturn(teachers);
        assertEquals(teachers, teacherDao.getAll());
    }
    
    @Test
    void testDelete_ShouldDeleteOneTeacher_WhenRequestedByTeacherIdAsParam() {
        Teacher teacher = new Teacher();
        int teacherId = 0;
        teacher.setId(teacherId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_TEACHER, teacherId)).thenReturn(1);
        assertTrue(teacherDao.delete(teacher));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroTeachers_WhenRequestedByTeacherIdAsParam() {
        Teacher teacher = new Teacher();
        int teacherId = 0;
        teacher.setId(teacherId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_TEACHER, teacherId)).thenReturn(0);
        assertFalse(teacherDao.delete(teacher));
    }

    @Test
    void testUpdate_ShouldReturnTrue_WhenRequestedByTeacherAsParam() {
        int teacherId = 0;
        String firstName = "Victor";
        String lastName = "Ivanov";
        
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        
        Mockito.when(jdbcTemplate.update(SQL_UPDATE_TEACHER, firstName, lastName, teacherId)).thenReturn(1);
        assertTrue(teacherDao.update(teacher));
    }
    
    @Test
    void testUpdate_ShouldReturnFalse_WhenRequestedByTeacherAsParam() {
        int teacherId = 0;
        String firstName = "Victor";
        String lastName = "Ivanov";
        
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        
        Mockito.when(jdbcTemplate.update(SQL_UPDATE_TEACHER, firstName, lastName, teacherId)).thenReturn(0);
        assertFalse(teacherDao.update(teacher));
    }

    @Test
    void testCreate_ShouldReturnTrue_WhenRequestedByTeacherAsParam() {
        int teacherId = 0;
        String firstName = "Victor";
        String lastName = "Ivanov";
        
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_TEACHER, teacherId, firstName, lastName)).thenReturn(1); 
        assertTrue(teacherDao.create(teacher));
    }
    
    @Test
    void testCreate_ShouldReturnFalse_WhenRequestedByTeacherAsParam() {
        int teacherId = 0;
        String firstName = "Victor";
        String lastName = "Ivanov";
        
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_TEACHER, teacherId, firstName, lastName)).thenReturn(1); 
        assertTrue(teacherDao.create(teacher));
    }
}
