package ua.com.foxminded.university.spring.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

import ua.com.foxminded.university.models.Subject;
import ua.com.foxminded.university.spring.dao.SubjectDao;
import ua.com.foxminded.university.spring.dao.mappers.SubjectMapper;

class SubjectDaoImplTest {
    
    @Mock
    JdbcTemplate jdbcTemplate;
    SubjectDao subjectDao = new SubjectDaoImpl(jdbcTemplate);
    
    private SubjectDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(subjectDao, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void testGetById_ShouldReturnSubject_WhenRequestedBySubjectIdAsParam() {    
        for(int subjectId = 0; subjectId < 5; subjectId++) {
            Subject subject = new Subject();
            subject.setId(subjectId);
            String sqlSelectById = "SELECT * FROM subjects WHERE subject_id = ?";
            Mockito.when(jdbcTemplate.queryForObject(eq(sqlSelectById), eq(new Object[] {subjectId}), any(SubjectMapper.class))).thenReturn(subject); 
        }
        for(int expectedId = 0; expectedId < 5; expectedId++) {
            Subject actualSubject = subjectDao.getById(expectedId);
            int actualId = actualSubject.getId();
            assertEquals(expectedId, actualId);
        }  
    }
    
    @Test
    void testGetAll_ShouldReturnListOfSubjects_WhenRequestedWithNoParam() {
        List<Subject> subjects = new ArrayList<>();
        for(int subjectId = 0; subjectId < 5; subjectId++) {
            Subject subject = new Subject();
            subject.setId(subjectId);
            subjects.add(subject);
        }
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM subjects"), any(SubjectMapper.class))).thenReturn(subjects);
        assertEquals(subjects, subjectDao.getAll());
    }
    
    @Test
    void testDelete_ShouldDeleteOneSubject_WhenRequestedBySubjectIdAsParam() {
        Subject subject = new Subject();
        int subjectId = 0;
        subject.setId(subjectId);
        Mockito.when(jdbcTemplate.update("DELETE FROM subjects WHERE subject_id = ?", subjectId)).thenReturn(1);
        assertTrue(subjectDao.delete(subject));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroSubjects_WhenRequestedBySubjectIdAsParam() {
        Subject subject = new Subject();
        int subjectId = 3;
        subject.setId(subjectId);
        Mockito.when(jdbcTemplate.update("DELETE FROM subjects WHERE subject_id = ?", subjectId)).thenReturn(0);
        assertFalse(subjectDao.delete(subject));
    }
    
    @Test
    void testUpdate_ShouldReturnTrue_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        String sqlUpdSubject = "UPDATE subjects SET name = ? WHERE subject_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdSubject, subjectName, subjectId)).thenReturn(1);
        assertTrue(subjectDao.update(subject));
    }
    
    @Test
    void testUpdate_ShouldReturnFalse_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        String sqlUpdSubject = "UPDATE subjects SET name = ? WHERE subject_id = ?";
        Mockito.when(jdbcTemplate.update(sqlUpdSubject, subjectName, subjectId)).thenReturn(0);
        assertFalse(subjectDao.update(subject));
    }
    
    @Test
    void testCreate_ShouldReturnTrue_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        String sqlInsertSubject = "INSERT INTO subjects(subject_id, name) values(?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertSubject, subjectId, subjectName)).thenReturn(1); 
        assertTrue(subjectDao.create(subject));
    }
    
    @Test
    void testCreate_ShouldReturnFalse_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        String sqlInsertSubject = "INSERT INTO subjects(subject_id, name) values(?,?)";
        Mockito.when(jdbcTemplate.update(sqlInsertSubject, subjectId, subjectName)).thenReturn(0); 
        assertFalse(subjectDao.create(subject));
    }
}
