package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.planner.Teacher;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

@Component
public class TeacherDaoImpl implements ua.com.foxminded.university.spring.dao.TeacherDao {
    
    JdbcTemplate jdbcTemplate;
    
    private final String SQL_GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
    private final String SQL_GET_ALL = "SELECT * FROM teachers";
    private final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE teacher_id = ?";
    private final String SQL_UPDATE_TEACHER = "UPDATE teachers SET first_name = ?, last_name = ? WHERE teacher_id = ?";
    private final String SQL_INSERT_TEACHER = "INSERT INTO teachers(teacher_id, first_name, last_name) values(?,?,?)";

    @Autowired
    public TeacherDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    


    @Override
    public Teacher getById(int teacherId) {
        return jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, new Object[] {teacherId}, new TeacherMapper());
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new TeacherMapper());
    }

    @Override
    public boolean delete(Teacher teacher) {
        return jdbcTemplate.update(SQL_DELETE_TEACHER, teacher.getId()) > 0;
    }

    @Override
    public boolean update(Teacher teacher) {
        return jdbcTemplate.update(SQL_UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getId()) > 0;
    }

    @Override
    public boolean create(Teacher teacher) {
        return jdbcTemplate.update(SQL_INSERT_TEACHER, teacher.getId(), teacher.getFirstName(), teacher.getLastName()) > 0;
    }

}
