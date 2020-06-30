package ua.com.foxminded.university.spring.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.models.Subject;

public class SubjectMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getInt("subject_id"));
        subject.setName(resultSet.getString("name"));
        return subject;
    }

}
