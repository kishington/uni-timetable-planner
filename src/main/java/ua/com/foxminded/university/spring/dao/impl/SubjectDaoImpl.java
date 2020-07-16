package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Subject;
import ua.com.foxminded.university.spring.dao.SubjectDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.mappers.SubjectMapper;

@Component
public class SubjectDaoImpl implements SubjectDao {

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
    
    private static final String UNABLE_GET_SUBJECT_BY_ID = "Unable to get subject by id from the database.";
    private static final String UNABLE_GET_ALL_SUBJECTS = "Unable to get all subjects from the database.";
    private static final String UNABLE_DELETE_SUBJECT = "Unable to delete subject from the database.";
    private static final String UNABLE_UPDATE_SUBJECT = "Unable to update subject in the database.";
    private static final String UNABLE_CREATE_SUBJECT = "Unable to insert subject in the database.";

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public SubjectDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subject getById(int subjectId) throws DatabaseException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_ID, new Object[] { subjectId }, new SubjectMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_SUBJECT_BY_ID, e);
        }
    }

    @Override
    public List<Subject> getAll() throws DatabaseException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new SubjectMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_ALL_SUBJECTS, e);
        }
    }

    @Override
    public boolean delete(Subject subject) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_DELETE_SUBJECT, subject.getId()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_DELETE_SUBJECT, e);
        }
    }

    @Override
    public boolean update(Subject subject) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_UPDATE_SUBJECT, subject.getName(), subject.getId()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_UPDATE_SUBJECT, e);
        }
    }

    @Override
    public boolean create(Subject subject) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_INSERT_SUBJECT, subject.getId(), subject.getName()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_CREATE_SUBJECT, e);
        }
    }

}
