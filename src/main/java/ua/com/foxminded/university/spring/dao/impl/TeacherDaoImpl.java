package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

@Component
public class TeacherDaoImpl implements TeacherDao {
    
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
    
    private static final String UNABLE_GET_TEACHER_BY_ID = "Unable to get teacher by id from the database.";
    private static final String UNABLE_GET_ALL_TEACHERS = "Unable to get all teachers from the database.";
    private static final String UNABLE_DELETE_TEACHER = "Unable to delete teacher from the database.";
    private static final String UNABLE_UPDATE_TEACHER = "Unable to update teacher in the database.";
    private static final String UNABLE_CREATE_TEACHER = "Unable to insert teacher in the database.";
    
    private JdbcTemplate jdbcTemplate;
   
    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher getById(int teacherId) throws DatabaseException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, new Object[] {teacherId}, new TeacherMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_TEACHER_BY_ID, e);
        }
    }

    @Override
    public List<Teacher> getAll() throws DatabaseException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new TeacherMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_ALL_TEACHERS, e);
        }
    }

    @Override
    public boolean delete(Teacher teacher) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_DELETE_TEACHER, teacher.getId()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_DELETE_TEACHER, e);
        }
    }

    @Override
    public boolean update(Teacher teacher) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getId()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_UPDATE_TEACHER, e);
        }
    }

    @Override
    public boolean create(Teacher teacher) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_INSERT_TEACHER, teacher.getId(), teacher.getFirstName(), teacher.getLastName()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_CREATE_TEACHER, e);
        }
    }

}
