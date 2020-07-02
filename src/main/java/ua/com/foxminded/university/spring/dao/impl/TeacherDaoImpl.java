package ua.com.foxminded.university.spring.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.models.Teacher;
import ua.com.foxminded.university.spring.dao.TeacherDao;
import ua.com.foxminded.university.spring.dao.mappers.TeacherMapper;

@Component
public class TeacherDaoImpl implements TeacherDao {
    
    private static final String SQL_GET_TEACHER_BY_ID = "" + 
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
            "  teachers\n";
    private static final String SQL_DELETE_TEACHER = "" +
            "delete from\n" + 
            "  teachers\n" + 
            "where\n" + 
            "  teacher_id = ?\n";
    private static final String SQL_UPDATE_TEACHER = "" +
            "update\n" + 
            "  teachers\n" + 
            "set\n" + 
            "  first_name = ?,\n" + 
            "  last_name = ?\n" + 
            "where\n" + 
            "  teacher_id = ?\n"; 
    private static final String SQL_INSERT_TEACHER = "" +
            "insert into teachers(" + 
            "  teacher_id, first_name, last_name\n" + 
            ")\n" + 
            "values\n" + 
            "  (?, ?, ?)\n"; 
    
    private JdbcTemplate jdbcTemplate;
   
    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
