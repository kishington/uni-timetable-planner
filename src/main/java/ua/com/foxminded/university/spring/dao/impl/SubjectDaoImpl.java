package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Subject;
import ua.com.foxminded.university.spring.dao.SubjectDao;
import ua.com.foxminded.university.spring.dao.mappers.SubjectMapper;

@Component
public class SubjectDaoImpl implements SubjectDao {

    private static final String SQL_GET_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE subject_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM subjects";
    private static final String SQL_DELETE_SUBJECT = "DELETE FROM subjects WHERE subject_id = ?";
    private static final String SQL_UPDATE_SUBJECT = "UPDATE subjects SET name = ? WHERE subject_id = ?";
    private static final String SQL_INSERT_SUBJECT = "INSERT INTO subjects(subject_id, name) values(?,?)";

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public SubjectDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subject getById(int subjectId) {
        return jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_ID, new Object[] { subjectId }, new SubjectMapper());
    }

    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new SubjectMapper());
    }

    @Override
    public boolean delete(Subject subject) {
        return jdbcTemplate.update(SQL_DELETE_SUBJECT, subject.getId()) > 0;
    }

    @Override
    public boolean update(Subject subject) {
        return jdbcTemplate.update(SQL_UPDATE_SUBJECT, subject.getName(), subject.getId()) > 0;
    }

    @Override
    public boolean create(Subject subject) {
        return jdbcTemplate.update(SQL_INSERT_SUBJECT, subject.getId(), subject.getName()) > 0;
    }

}
