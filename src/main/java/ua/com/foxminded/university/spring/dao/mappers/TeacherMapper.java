package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.models.Teacher;

public class TeacherMapper implements RowMapper<Teacher> {

    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getInt("teacher_id"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
        return teacher;
    }
    
}

