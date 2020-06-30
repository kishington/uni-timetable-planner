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
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

class TeacherDaoImplTest {
    
    @Mock
    JdbcTemplate jdbcTemplate;
    TeacherDaoImpl teacherDao = new TeacherDaoImpl(jdbcTemplate);
    
    private TeacherDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(teacherDao, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void testGetById_ShouldReturnteacher_WhenRequestedByTeacherIdAsParam() {  
        for(int teacherId = 0; teacherId < 5; teacherId++) {
            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            String sqlSelectById = "SELECT * FROM teachers WHERE teacher_id = ?";
            Mockito.when(jdbcTemplate.queryForObject(eq(sqlSelectById), eq(new Object[] {teacherId}), any(TeacherMapper.class))).thenReturn(teacher); 
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
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM teachers"), any(TeacherMapper.class))).thenReturn(teachers);
        assertEquals(teachers, teacherDao.getAll());
    }
    
    @Test
    void testDelete_ShouldDeleteOneTeacher_WhenRequestedByTeacherIdAsParam() {
        Teacher teacher = new Teacher();
        int teacherId = 0;
        teacher.setId(teacherId);
        Mockito.when(jdbcTemplate.update("DELETE FROM teachers WHERE teacher_id = ?", teacherId)).thenReturn(1);
        assertTrue(teacherDao.delete(teacher));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroTeachers_WhenRequestedByTeacherIdAsParam() {
        Teacher teacher = new Teacher();
        int teacherId = 0;
        teacher.setId(teacherId);
        Mockito.when(jdbcTemplate.update("DELETE FROM teachers WHERE teacher_id = ?", teacherId)).thenReturn(0);
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
        
        String sqlUpdTeacher = "UPDATE teachers SET first_name = ?, last_name = ? WHERE teacher_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdTeacher, firstName, lastName, teacherId)).thenReturn(1);
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
        
        String sqlUpdTeacher = "UPDATE teachers SET first_name = ?, last_name = ? WHERE teacher_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdTeacher, firstName, lastName, teacherId)).thenReturn(0);
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
        
        String sqlInsertSubject = "INSERT INTO teachers(teacher_id, first_name, last_name) values(?,?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertSubject, teacherId, firstName, lastName)).thenReturn(1); 
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
        
        String sqlInsertSubject = "INSERT INTO teachers(teacher_id, first_name, last_name) values(?,?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertSubject, teacherId, firstName, lastName)).thenReturn(1); 
        assertTrue(teacherDao.create(teacher));
    }
}
