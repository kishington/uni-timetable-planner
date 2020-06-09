package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.planner.Group;
import ua.com.foxminded.university.planner.Lesson;
import ua.com.foxminded.university.planner.Subject;
import ua.com.foxminded.university.planner.Teacher;
import ua.com.foxminded.university.planner.Timeslot;

public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("lesson_id"));
        
        Subject subject = new Subject();
        subject.setId(resultSet.getInt("subject_id"));
        lesson.setSubject(subject);
        
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getInt("teacher_id"));
        lesson.setTeacher(teacher);
        
        Group group = new Group();
        group.setId(resultSet.getInt("group_id"));
        lesson.setGroup(group);
        
        String day = resultSet.getString("day");
        day = day.toUpperCase();
        lesson.setDay(DayOfWeek.valueOf(day));
        
        int timeslotId = resultSet.getInt("timeslot_id");
        Timeslot timeslot = Timeslot.getTimeslotById(timeslotId);
        lesson.setTimeslot(timeslot);
        
        return lesson;
    }

}
