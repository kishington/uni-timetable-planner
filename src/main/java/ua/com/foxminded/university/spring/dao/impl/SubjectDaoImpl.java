package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Subject;
import ua.com.foxminded.university.spring.dao.SubjectDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.exception.ObjectNotFoundException;
import ua.com.foxminded.university.spring.dao.mappers.SubjectMapper;

@Component
public class SubjectDaoImpl implements SubjectDao {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectDaoImpl.class);
    
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
    private static final String QUERY_EXECUTION_WENT_WRONG= "Something went wrong during SQL Query execution.";

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public SubjectDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subject getById(int subjectId) {
        
        LOG.debug("Receiving subject: subjectId = {}", subjectId);
        
        try {
            Subject subject = jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_ID, new Object[] { subjectId }, new SubjectMapper());
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received subject: {}", subject);
            }
            
            return subject;
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_SUBJECT_BY_ID, e);
            
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
    public List<Subject> getAll() {
        
        LOG.debug("Receiving all subjects");
        
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new SubjectMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_ALL_SUBJECTS, e);
           
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
    public boolean delete(Subject subject) {
        
        LOG.debug("Deleting subject: subjectId = {}", subject.getId());
        
        try {
            boolean isSubjectDeleted = jdbcTemplate.update(SQL_DELETE_SUBJECT, subject.getId()) > 0;

            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isSubjectDeleted) {
                    logMessage = "Deleted subject: " + subject;
                } else {
                    logMessage = "Subject not deleted: " + subject;
                }
                LOG.debug(logMessage);
            }
            
            return isSubjectDeleted;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_DELETE_SUBJECT, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean update(Subject subject) {
        
        LOG.debug("Updating subject: subjectId = {}", subject.getId());
        
        try {
            boolean isSubjectUpdated = jdbcTemplate.update(SQL_UPDATE_SUBJECT, subject.getName(), subject.getId()) > 0;

            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isSubjectUpdated) {
                    logMessage = "Updated subject: " + subject;
                } else {
                    logMessage = "Subject not updated: " + subject;
                }
                LOG.debug(logMessage);
            }
            
            return isSubjectUpdated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_UPDATE_SUBJECT, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean create(Subject subject) {
        
        LOG.debug("Creating subject: subjectId = {}", subject.getId());
        
        try {
            boolean isSubjectCreated = jdbcTemplate.update(SQL_INSERT_SUBJECT, subject.getId(), subject.getName()) > 0;
           
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isSubjectCreated) {
                    logMessage = "Created subject: " + subject;
                } else {
                    logMessage = "Subject not created: " + subject;
                }
                LOG.debug(logMessage);
            }
           
            return isSubjectCreated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_CREATE_SUBJECT, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

}
