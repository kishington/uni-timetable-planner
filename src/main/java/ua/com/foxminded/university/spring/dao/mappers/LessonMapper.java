package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.models.Lesson;

public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("lesson_id"));
        
        int subjectId = resultSet.getInt("subject_id");
        lesson.setSubjectId(subjectId);
        
        int teacherId = resultSet.getInt("teacher_id");
        lesson.setTeacherId(teacherId);
        
        int groupId = resultSet.getInt("group_id");
        lesson.setGroupId(groupId);
        
        String day = resultSet.getString("day");
        if (day != null) {
            day = day.toUpperCase();
            lesson.setDay(DayOfWeek.valueOf(day));
        }
        
        int timeslotId = resultSet.getInt("timeslot_id");
        lesson.setTimeslotId(timeslotId);
        
        return lesson;
    }

}
