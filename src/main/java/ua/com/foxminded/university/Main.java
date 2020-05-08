package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.planner.Teacher;
import ua.com.foxminded.university.spring.config.AppConfig;
import ua.com.foxminded.university.spring.dao.TeacherDao;

public class Main {

    public static void main(String[] args) {
        
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TeacherDao teacherDao = context.getBean(TeacherDao.class);
        
        Teacher teacher = new Teacher(2, "Ivan", "Susanin");
        teacherDao.create(teacher);
        teacher = new Teacher(3, "Super", "Jorik");
        teacherDao.create(teacher);

        System.out.println("List of person is:");

        for (Teacher p : teacherDao.getAll()) {
            System.out.println(p);
        }

        System.out.println("\nGet person with ID 2");
        Teacher teacherById = teacherDao.getById(2);
        System.out.println(teacherById);

        System.out.println("\nCreating person: ");
        teacher = new Teacher(4, "Sergey", "Emets");
        System.out.println(teacher);
        teacherDao.create(teacher);
        
        System.out.println("\nList of person is:");
        for (Teacher p : teacherDao.getAll()) {
            System.out.println(p);
        }

        System.out.println("\nDeleting person with ID 2");
        teacherDao.delete(teacherById);

        System.out.println("\nUpdate person with ID 4");

        Teacher teacher2 = teacherDao.getById(4);
        teacher2.setLastName("CHANGED");
        teacherDao.update(teacher2);

        System.out.println("\nList of person is:");
        for (Teacher p : teacherDao.getAll()) {
            System.out.println(p);
        }

        context.close();
    }

}
