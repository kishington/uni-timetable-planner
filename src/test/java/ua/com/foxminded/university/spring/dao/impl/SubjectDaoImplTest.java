package ua.com.foxminded.university.spring.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.SubjectDao;

class SubjectDaoImplTest {
    
    @Mock
    JdbcTemplate jdbcTemplate;
    SubjectDao lessonDao = new SubjectDaoImpl();
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(lessonDao, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void testSubjectDaoImpl() {
        fail("Not yet implemented");
    }

    @Test
    void testGetById() {
        fail("Not yet implemented");
    }

    @Test
    void testGetAll() {
        fail("Not yet implemented");
    }

    @Test
    void testDelete() {
        fail("Not yet implemented");
    }

    @Test
    void testUpdate() {
        fail("Not yet implemented");
    }

    @Test
    void testCreate() {
        fail("Not yet implemented");
    }

}
