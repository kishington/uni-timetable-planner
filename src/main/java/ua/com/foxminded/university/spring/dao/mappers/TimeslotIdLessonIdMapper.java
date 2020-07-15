package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.spring.dao.util.TimeslotIdLessonIdPair;

public class TimeslotIdLessonIdMapper implements RowMapper<TimeslotIdLessonIdPair> {
    
    @Override
    public TimeslotIdLessonIdPair mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int timeslotId = resultSet.getInt("timeslot_id");
        int lessonId = resultSet.getInt("lesson_id");  
        return new TimeslotIdLessonIdPair(timeslotId, lessonId);
    }
}
