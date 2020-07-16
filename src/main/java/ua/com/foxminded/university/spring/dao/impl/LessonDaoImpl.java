package ua.com.foxminded.university.spring.dao.impl;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.mappers.TimeslotIdLessonIdMapper;
import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

@Component
public class LessonDaoImpl implements LessonDao {
    
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

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public LessonDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Lesson getById(int lessonId) throws DatabaseException {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new Object[] { lessonId }, new LessonMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_LESSON_BY_ID, e);
        }
    }

    @Override
    public List<Lesson> getAll() throws DatabaseException {
        try {
            return jdbcTemplate.query(SQL_GET_ALL, new LessonMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_ALL_LESSONS, e);
        }
    }

    @Override
    public boolean delete(Lesson lesson) throws DatabaseException {
        try {
            return jdbcTemplate.update(SQL_DELETE_LESSON, lesson.getId()) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_DELETE_LESSON, e);
        }
    }

    @Override
    public boolean update(Lesson lesson) throws DatabaseException {
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);     
        try {
            return jdbcTemplate.update(SQL_UPDATE_LESSON, subjectId, teacherId, groupId, day, timeslotId, lessonId) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_UPDATE_LESSON, e);
        }
    }
    
    @Override
    public boolean create(Lesson lesson) throws DatabaseException {
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        try {
            return jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId) > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_CREATE_LESSON, e);
        }
    }
    
    public int getNumberOfLessonsPerWeekForTeacher(int teacherId) throws DatabaseException {      
        try {
            return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER, new Object[] { teacherId }, Integer.class);
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_COUNT_LESSONS_FOR_TEACHER, e);
        }
    }

    public int getNumberOfLessonsPerWeekForGroup(int groupId) throws DatabaseException {      
        try {
            return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP, new Object[] { groupId }, Integer.class);
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_COUNT_LESSONS_FOR_GROUP, e);
        }
    }

    public List<Lesson> getAllLessonsForDay(int groupId, String day) throws DatabaseException {
        day = day.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY, new Object[] { groupId, day }, new LessonMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_ALL_LESSONS_FOR_GROUP_FOR_DAY, e);
        }
    } 
    
    public List<Lesson> getAllTeacherLessonsForWeek(int teacherId) throws DatabaseException {
        try {
            return jdbcTemplate.query(SQL_GET_LESSONS_FOR_TEACHER_FOR_WEEK, new Object[] { teacherId }, new LessonMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_ALL_LESSONS_FOR_TEACHER_FOR_WEEK, e);
        }
    }
    
    public List<TimeslotIdLessonIdPair> getTeachersTimeslotIdAndLessonIdPairs(int teacherId, DayOfWeek day) throws DatabaseException {
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_TEACHER_DAY_TIMETABLE, new Object[] { teacherId, dayString }, new TimeslotIdLessonIdMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_TEACHERS_DAY_TIMETABLE, e);
        }
    }
    
    public List<TimeslotIdLessonIdPair> getGroupsTimeslotIdAndLessonIdPairs(int groupId, DayOfWeek day) throws DatabaseException {
        String dayString = day.getDisplayName(TextStyle.FULL, Locale.UK);
        dayString = dayString.toUpperCase();
        try {
            return jdbcTemplate.query(SQL_GET_GROUP_DAY_TIMETABLE, new Object[] { groupId, dayString }, new TimeslotIdLessonIdMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException(UNABLE_GET_GROUPS_DAY_TIMETABLE, e);
        }
    }
}
