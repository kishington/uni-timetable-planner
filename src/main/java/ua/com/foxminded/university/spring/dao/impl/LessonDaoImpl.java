package ua.com.foxminded.university.spring.dao.impl;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.planner.Lesson;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

@Component
public class LessonDaoImpl implements LessonDao {

    private JdbcTemplate jdbcTemplate;
    
    private final String SQL_GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE lesson_id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM lessons";
    private final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE lesson_id = ?";
    private final String SQL_UPDATE_LESSON = "UPDATE lessons SET subject_id = ?, teacher_id = ?, group_id = ?, day = ?, timeslot_id = ? " +
                                             "WHERE lesson_id = ?";
    private final String SQL_INSERT_LESSON = "INSERT INTO lessons(lesson_id, subject_id, teacher_id, group_id, day, timeslot_id) values(?,?,?,?,?,?)";
    
    @Autowired
    public LessonDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public Lesson getById(int lessonId) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new Object[] {lessonId}, new LessonMapper());
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new LessonMapper());
    }

    @Override
    public boolean delete(Lesson lesson) {
        return jdbcTemplate.update(SQL_DELETE_LESSON, lesson.getId()) > 0;
    }

    @Override
    public boolean update(Lesson lesson) {
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        int timeslotId = lesson.getTimeslot().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);     
        return jdbcTemplate.update(SQL_UPDATE_LESSON, subjectId, teacherId, groupId, day, timeslotId, lessonId) > 0;
    }
    
    @Override
    public boolean create(Lesson lesson) {
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubject().getId();
        int teacherId = lesson.getTeacher().getId();
        int groupId = lesson.getGroup().getId();
        int timeslotId = lesson.getTimeslot().getId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        return jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId) > 0;
    }

}
