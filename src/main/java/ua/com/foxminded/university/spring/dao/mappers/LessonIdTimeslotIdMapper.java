package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LessonIdTimeslotIdMapper implements RowMapper<int[]> {
    
    @Override
    public int[] mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int[] timeslotIdAndLessonId = new int[2];
        timeslotIdAndLessonId[0] = resultSet.getInt("timeslot_id");
        timeslotIdAndLessonId[1] = resultSet.getInt("lesson_id");      
        return timeslotIdAndLessonId;
    }
}
