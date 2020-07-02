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

import ua.com.foxminded.university.models.Subject;
import ua.com.foxminded.university.spring.dao.SubjectDao;
import ua.com.foxminded.university.spring.dao.mappers.SubjectMapper;

class SubjectDaoImplTest {
    
    private static final String SQL_GET_SUBJECT_BY_ID = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  subjects\n" + 
            "where\n" + 
            "  subject_id = ?\n";
    private static final String SQL_GET_ALL = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  subjects\n";
    private static final String SQL_DELETE_SUBJECT = "" +
            "delete from\n" + 
            "  subjects\n" + 
            "where\n" + 
            "  subject_id = ?\n";
    private static final String SQL_UPDATE_SUBJECT = "" +
            "update\n" + 
            "  subjects\n" + 
            "set\n" + 
            "  name = ?\n" + 
            "where\n" + 
            "  subject_id = ?\n"; 
    private static final String SQL_INSERT_SUBJECT = "" +
            "insert into subjects(subject_id, name)\n" + 
            "values\n" + 
            "  (?, ?)\n"; 
    
    @Mock
    JdbcTemplate jdbcTemplate;
    SubjectDao subjectDao;
    
    private SubjectDaoImplTest() {
        MockitoAnnotations.initMocks(this);
        subjectDao = new SubjectDaoImpl(jdbcTemplate);
    }

    @Test
    void testGetById_ShouldReturnSubject_WhenRequestedBySubjectIdAsParam() {    
        for(int subjectId = 0; subjectId < 5; subjectId++) {
            Subject subject = new Subject();
            subject.setId(subjectId);
            Mockito.when(jdbcTemplate.queryForObject(eq(SQL_GET_SUBJECT_BY_ID), eq(new Object[] {subjectId}), any(SubjectMapper.class))).thenReturn(subject); 
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
        Mockito.when(jdbcTemplate.query(eq(SQL_GET_ALL), any(SubjectMapper.class))).thenReturn(subjects);
        assertEquals(subjects, subjectDao.getAll());
    }
    
    @Test
    void testDelete_ShouldDeleteOneSubject_WhenRequestedBySubjectIdAsParam() {
        Subject subject = new Subject();
        int subjectId = 0;
        subject.setId(subjectId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_SUBJECT, subjectId)).thenReturn(1);
        assertTrue(subjectDao.delete(subject));
    }
    
    @Test
    void testDelete_ShouldDeleteZeroSubjects_WhenRequestedBySubjectIdAsParam() {
        Subject subject = new Subject();
        int subjectId = 3;
        subject.setId(subjectId);
        Mockito.when(jdbcTemplate.update(SQL_DELETE_SUBJECT, subjectId)).thenReturn(0);
        assertFalse(subjectDao.delete(subject));
    }
    
    @Test
    void testUpdate_ShouldReturnTrue_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        Mockito.when(jdbcTemplate.update(SQL_UPDATE_SUBJECT, subjectName, subjectId)).thenReturn(1);
        assertTrue(subjectDao.update(subject));
    }
    
    @Test
    void testUpdate_ShouldReturnFalse_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        Mockito.when(jdbcTemplate.update(SQL_UPDATE_SUBJECT, subjectName, subjectId)).thenReturn(0);
        assertFalse(subjectDao.update(subject));
    }
    
    @Test
    void testCreate_ShouldReturnTrue_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_SUBJECT, subjectId, subjectName)).thenReturn(1); 
        assertTrue(subjectDao.create(subject));
    }
    
    @Test
    void testCreate_ShouldReturnFalse_WhenRequestedBySubjectAsParam() {
        int subjectId = 0;
        String subjectName = "Maths";
        
        Subject subject = new Subject();
        subject.setId(subjectId);
        subject.setName(subjectName);
        
        Mockito.when(jdbcTemplate.update(SQL_INSERT_SUBJECT, subjectId, subjectName)).thenReturn(0); 
        assertFalse(subjectDao.create(subject));
    }
}
