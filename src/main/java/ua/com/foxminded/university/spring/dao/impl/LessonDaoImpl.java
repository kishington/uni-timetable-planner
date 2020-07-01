package ua.com.foxminded.university.spring.dao.impl;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.spring.dao.LessonDao;
import ua.com.foxminded.university.spring.dao.mappers.LessonMapper;

@Component
public class LessonDaoImpl implements LessonDao {
    
    private static final String SQL_GET_LESSON_BY_ID = "" + 
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
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public LessonDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Lesson getById(int lessonId) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new Object[] { lessonId }, new LessonMapper());
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
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK);     
        return jdbcTemplate.update(SQL_UPDATE_LESSON, subjectId, teacherId, groupId, day, timeslotId, lessonId) > 0;
    }
    
    @Override
    public boolean create(Lesson lesson) {
        int lessonId = lesson.getId();
        int subjectId = lesson.getSubjectId();
        int teacherId = lesson.getTeacherId();
        int groupId = lesson.getGroupId();
        int timeslotId = lesson.getTimeslotId();
        String day = lesson.getDay().getDisplayName(TextStyle.FULL, Locale.UK); 
        return jdbcTemplate.update(SQL_INSERT_LESSON, lessonId, subjectId, teacherId, groupId, day, timeslotId) > 0;
    }
    
    public int getNumberOfLessonsPerWeekForTeacher(int teacherId) {      
        return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_TEACHER, new Object[] { teacherId }, Integer.class);
    }

    public int getNumberOfLessonsPerWeekForGroup(int groupId) {      
        return jdbcTemplate.queryForObject(SQL_GET_NO_OF_LESSONS_PER_WEEK_FOR_GROUP, new Object[] { groupId }, Integer.class);
    }

    public List<Lesson> getAllLessonsForDay(int groupId, String day) {
        day = day.toUpperCase();
        return jdbcTemplate.query(SQL_GET_LESSONS_FOR_GROUP_FOR_GIVEN_DAY, new Object[] { groupId, day }, new LessonMapper());
    } 
}
