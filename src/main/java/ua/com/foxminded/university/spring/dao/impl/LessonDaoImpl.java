package ua.com.foxminded.university.spring.dao.impl;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.exception.ObjectNotFoundException;
import ua.com.foxminded.university.spring.dao.mappers.TimeslotIdLessonIdMapper;
import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

@Component
public class LessonDaoImpl implements LessonDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(LessonDaoImpl.class);
    
    private static final String SQL_GET_LESSON_BY_ID = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  lesson_id = ?\n";
    private static final String SQL_GET_ALL = "" + 
            "select\n" + 
            "  *\n" + 
            "from\n" + 
            "  lessons\n";
    private static final String SQL_DELETE_LESSON = "" +
            "delete from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  lesson_id = ?\n";
    private static final String SQL_UPDATE_LESSON = "" +
            "update\n" + 
            "  lessons\n" + 
            "set\n" + 
            "  subject_id = ?,\n" + 
            "  teacher_id = ?,\n" + 
            "  group_id = ?,\n" + 
            "  day = ?,\n" + 
            "  timeslot_id = ?\n" + 
            "where\n" + 
            "  lesson_id = ?\n"; 
    private static final String SQL_INSERT_LESSON = "" +
            "insert into lessons(\n" + 
            "  lesson_id, subject_id, teacher_id,\n" + 
            "  group_id, day, timeslot_id\n" + 
            ")\n" + 
            "values\n" + 
            "  (?, ?, ?, ?, ?, ?)\n"; 
    private static final String SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER = "" + 
            "select\n" + 
            "  count(*)\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
    private static final String SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP = "" + 
            "select\n" + 
            "  count(*)\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n";
    private static final String SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY = "" +
            "select\n" + 
            "  lesson_id,\n" +
            "  subject_id,\n" + 
            "  teacher_id,\n" + 
            "  group_id,\n" + 
            "  day,\n" + 
            "  timeslot_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n" + 
            "  and upper(day) = ?\n";
    private static final String SQL_GET_LESSONS_FOR_TEACHER_FOR_WEEK = "" + 
            "select\n" + 
            "  lesson_id,\n" + 
            "  subject_id,\n" + 
            "  teacher_id,\n" + 
            "  group_id,\n" + 
            "  day,\n" + 
            "  timeslot_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
    private static final String SQL_GET_TEACHER_DAY_TIMETABLE = "" +
            "select\n" + 
            "  timeslot_id,\n" + 
            "  lesson_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  teacher_id = ?\n" + 
            "  and upper(day) = ?\n";
    private static final String SQL_GET_GROUP_DAY_TIMETABLE = "" +
            "select\n" + 
            "  timeslot_id,\n" + 
            "  lesson_id\n" + 
            "from\n" + 
            "  lessons\n" + 
            "where\n" + 
            "  group_id = ?\n" + 
            "  and upper(day) = ?\n";
    
    private static final String UNABLE_GET_LESSON_BY_ID = "Unable to get lesson by id from the database.";
    private static final String UNABLE_GET_ALL_LESSONS = "Unable to get all lessons from the database.";
    private static final String UNABLE_DELETE_LESSON = "Unable to delete lesson from the database.";
    private static final String UNABLE_UPDATE_LESSON = "Unable to update lesson in the database.";
    private static final String UNABLE_CREATE_LESSON = "Unable to insert create in the database.";
    private static final String UNABLE_COUNT_LESSONS_FOR_TEACHER = "Unable to count lessons for teacher in the database.";
    private static final String UNABLE_COUNT_LESSONS_FOR_GROUP = "Unable to count lessons for group in the database.";
    private static final String UNABLE_GET_ALL_LESSONS_FOR_GROUP_FOR_DAY = "Unable to get all lessons for group from the database.";
    private static final String UNABLE_GET_ALL_LESSONS_FOR_TEACHER_FOR_WEEK = "Unable to get all lessons for teacher from the database.";
    private static final String UNABLE_GET_TEACHERS_DAY_TIMETABLE = "Unable to get teachers day timetable from the database.";
    private static final String UNABLE_GET_GROUPS_DAY_TIMETABLE = "Unable to get groups day timetable from the database.";
    private static final String QUERY_EXECUTION_WENT_WRONG= "Something went wrong during SQL Query execution.";

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public LessonDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Lesson getById(int lessonId) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving lesson: lessonId = {}", lessonId);
        }
        
        try {
            Lesson lesson = jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new Object[] { lessonId }, new LessonMapper());
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received lesson: {}", lesson);
            }
            
            return lesson;
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_LESSON_BY_ID, e);
            
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
    public List<Lesson> getAll() {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving all lessons");
        }
        
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new LessonMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_ALL_LESSONS, e);
            
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
    public boolean delete(Lesson lesson) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting lesson: lessonId = {}", lesson.getId());
        }
        
        try {
            boolean isLessonDeleted = jdbcTemplate.update(SQL_DELETE_LESSON, lesson.getId()) > 0;
            
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isLessonDeleted) {
                    logMessage = "Deleted lesson: " + lesson;
                } else {
                    logMessage = "Lesson not deleted: " + lesson;
                }
                LOG.debug(logMessage);
            }
            
            return isLessonDeleted;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_DELETE_LESSON, e);
            
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    @Override
    public boolean update(Lesson lesson) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating lesson: lessonId = {}", lesson.getId());
        }
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);     
        try {
            boolean isLessonUpdated = jdbcTemplate.update(SQL_UPDATE_LESSON, subjectId, teacherId, groupId, day, timeslotId, lessonId) > 0;
            
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isLessonUpdated) {
                    logMessage = "Updated lesson: " + lesson;
                } else {
                    logMessage = "Lesson not updated: " + lesson;
                }
                LOG.debug(logMessage);
            }
            
            return isLessonUpdated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_UPDATE_LESSON, e);
           
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }
    
    @Override
    public boolean create(Lesson lesson) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating lesson: lessonId = ", lesson.getId());
        }
        
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        try {
            boolean isLessonCreated = jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId) > 0;
            
            if (LOG.isDebugEnabled()) {
                String logMessage;
                if (isLessonCreated) {
                    logMessage = "Created lesson: " + lesson;
                } else {
                    logMessage = "Lesson not created: " + lesson;
                }
                LOG.debug(logMessage);
            }
            
            return isLessonCreated;
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_CREATE_LESSON, e);
            
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }
    
    public int getNumberOfLessonsPerWeekForTeacher(int teacherId) {     
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving number of lessons per week for teacher: teacherId = {}", teacherId);
        }
        
        try {
            return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER, new Object[] { teacherId }, Integer.class);
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_COUNT_LESSONS_FOR_TEACHER, e);
            
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    public int getNumberOfLessonsPerWeekForGroup(int groupId) {     
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving number of lessons per week for group: groupId = {}", groupId);
        }
        
        try {
            return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP, new Object[] { groupId }, Integer.class);
        } catch (DataAccessException e) {
            DatabaseException rethrownException = new DatabaseException(UNABLE_COUNT_LESSONS_FOR_GROUP, e);
            
            LOG.error(rethrownException.getMessage(), rethrownException);
            
            throw rethrownException;
        }
    }

    public List<Lesson> getAllLessonsForDay(int groupId, String day) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving all lessons for day for group: groupId = {}, day = {}", groupId, day);
        }
        
        day = day.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY, new Object[] { groupId, day }, new LessonMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_ALL_LESSONS_FOR_GROUP_FOR_DAY, e);
           
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
    
    public List<Lesson> getAllTeacherLessonsForWeek(int teacherId) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving all lessons for week for teacher: groupId = {}", teacherId);
        }
        
        try {
            return jdbcTemplate.query(SQL_GET_LESSONS_FOR_TEACHER_FOR_WEEK, new Object[] { teacherId }, new LessonMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_ALL_LESSONS_FOR_TEACHER_FOR_WEEK, e);
            
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
    
    public List<TimeslotIdLessonIdPair> getTeachersTimeslotIdAndLessonIdPairs(int teacherId, DayOfWeek day) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving teacher's timeslotId and lessonId pairs: teacherId = {}, day = {}", teacherId, day);
        }
        
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_TEACHER_DAY_TIMETABLE, new Object[] { teacherId, dayString }, new TimeslotIdLessonIdMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_TEACHERS_DAY_TIMETABLE, e);
            
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
    
    public List<TimeslotIdLessonIdPair> getGroupsTimeslotIdAndLessonIdPairs(int groupId, DayOfWeek day) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Receiving group's timeslotId and lessonId pairs: groupId = {}, day = {}", groupId, day);
        }
        
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_GROUP_DAY_TIMETABLE, new Object[] { groupId, dayString }, new TimeslotIdLessonIdMapper());
        } catch (EmptyResultDataAccessException e) {
            ObjectNotFoundException rethrownException = new ObjectNotFoundException(UNABLE_GET_GROUPS_DAY_TIMETABLE, e);
           
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
}
