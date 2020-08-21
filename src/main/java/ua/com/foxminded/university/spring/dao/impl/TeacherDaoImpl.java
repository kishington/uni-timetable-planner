package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.exception.ObjectNotFoundException;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

@Component
public class TeacherDaoImpl implements TeacherDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(TeacherDaoImpl.class);
    
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
    private static final String QUERY_EXECUTION_WENT_WRONG= "Something went wrong during SQL Query execution.";
    
    private JdbcTemplate jdbcTemplate;
   
    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher getById(int teacherId) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving teacher: teachertId = {}", teacherId);
        }
        
        try {
            Teacher teacher = jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, new Object[] {teacherId}, new TeacherMapper());
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received subject: {}", teacher);
            }
            
            return teacher;
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_TEACHER_BY_ID, e);
            
            if (LOG.isInfoEnabled()) {
                LOG.info(rethrownException.getMessage(), rethrownException);
            }
            
            throw rethrownException;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(QUERY_EXECUTION_WENT_WRONG, e);
            
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public List<Teacher> getAll() {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving all teachers.");
        }
        
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new TeacherMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_ALL_TEACHERS, e);
           
            if (LOG.isInfoEnabled()) {
                LOG.info(rethrownException.getMessage(), rethrownException);
            }
            
            throw rethrownException;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(QUERY_EXECUTION_WENT_WRONG, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean delete(Teacher teacher) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting teacher: teachertId = {}", teacher.getId());
        }
        
        try {
            boolean isTeacherDeleted = jdbcTemplate.update(SQL_DELETE_TEACHER, teacher.getId()) > 0;
          
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isTeacherDeleted) {
                    logMessage = "Deleted teacher: " + teacher;
                } else {
                    logMessage = "Teacher not deleted: " + teacher;
                }
                LOG.debug(logMessage);
            }
           
            return isTeacherDeleted;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_DELETE_TEACHER, e);
         
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean update(Teacher teacher) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating teacher: teachertId = {}", teacher.getId());
        }
        
        try {
            boolean isTeacherUpdated = jdbcTemplate.update(SQL_UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getId()) > 0;
         
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isTeacherUpdated) {
                    logMessage = "Updated teacher: " + teacher;
                } else {
                    logMessage = "Teacher not updated: " + teacher;
                }
                LOG.debug(logMessage);
            }
         
            return isTeacherUpdated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_UPDATE_TEACHER, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean create(Teacher teacher) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating teacher: teachertId = {}", teacher.getId());
        }
        
        try {
            boolean isTeacherCreated = jdbcTemplate.update(SQL_INSERT_TEACHER, teacher.getId(), teacher.getFirstName(), teacher.getLastName()) > 0;
            
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isTeacherCreated) {
                    logMessage = "Created teacher: " + teacher;
                } else {
                    logMessage = "Teacher not created: " + teacher;
                }
                LOG.debug(logMessage);
            }
           
            return isTeacherCreated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_CREATE_TEACHER, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

}
