package ua.com.foxminded.university.spring.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ua.com.foxminded.university.spring.config.AppConfig;
import ua.com.foxminded.university.spring.config.AppConfigTest;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.TeacherDao;

class LessonDaoImplTest {
    
    static private AnnotationConfigApplicationContext testContext;
    static private LessonDao lessonDao;
    static public JdbcTemplate jdbcTemplate;
    
    static TeacherDao teacherDao;
    
    @BeforeAll
    static void setContext() {
        testContext = new AnnotationConfigApplicationContext(AppConfigTest.class);
        lessonDao = testContext.getBean(LessonDao.class);
        DataSource ds = testContext.getBean(DataSource.class);
        jdbcTemplate = new JdbcTemplate(ds);
        teacherDao = testContext.getBean(TeacherDao.class);
    }
    
    @AfterAll
    static void closeContext() {
        testContext.close();
    }
    
    @BeforeEach
    void deleteAllRows() {
        int rows = jdbcTemplate.update("DELETE FROM lessons");
        System.out.println(jdbcTemplate == null);
        
        // DELETE FROM lessons;
    }
    
    @Test
    void test() {
        String[] allBeanNames = testContext.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
        }
        
        
        DriverManagerDataSource ds = (DriverManagerDataSource)testContext.getBean(DataSource.class);
        System.out.println(ds == null);
        System.out.println("kuku " + ds.getUrl());
       // System.out.println(lessonDao.getAll());
        jdbcTemplate.update("INSERT INTO lessons(lesson_id, subject_id) values(1,2)");
        System.out.println(teacherDao.getAll());
        System.out.println(lessonDao.getAll());
        
    }

    @Test
    void testGetById() {
        // INSERT INTO lessons(lesson_id, subject_id, teacher_id, group_id, day, timeslot_id) values(5,4,6,7,'monday',3)
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
