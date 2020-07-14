import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Timeslot;
import ua.com.foxminded.university.models.Timetable;
import ua.com.foxminded.university.spring.config.AppConfig;
import ua.com.foxminded.university.spring.dao.impl.LessonDaoImpl;
import ua.com.foxminded.university.spring.service.TimetableService;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //context.refresh();
        String[] allBeanNames = context.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
        }
        int teacherId = 0;
        LessonDaoImpl lessonDao = context.getBean(LessonDaoImpl.class);
        List<Lesson> lessons = lessonDao.getAllTeacherLessonsForWeek(teacherId);
        for (Lesson l: lessons) {
            System.out.println(l);
        }
        System.out.println("\nkuku\n");
        Lesson testLesson = lessonDao.getById(1);
        System.out.println("testLesson: \n" + testLesson);
        
        
        System.out.println("\n\n");
       // Map<Timeslot, Integer> output = lessonDao.getTeacherTimetableForDay(0, DayOfWeek.MONDAY);
        TimetableService service = context.getBean(TimetableService.class);
        DayOfWeek day = DayOfWeek.MONDAY;
        System.out.println("Day Timetable:\nteacherId = " + teacherId + "; day = " + day + "\n");
        Map<Timeslot, Lesson> output = service.getDayTimetableForTeacher(teacherId, day);
        output.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + "\n" + entry.getValue());
        });
        //System.out.println(output);
        
        System.out.println("\nWeek Timetable:\nteacherId = " + teacherId + "\n");
        Timetable timetable = service.getWeekTimetableForTeacher(teacherId);
        Map<DayOfWeek, Map<Timeslot,Lesson>> map = timetable.getValue();
        map.forEach((dayy, dayTimetable) -> System.out.println("day = " + dayy + "\nday timetable:\n" + dayTimetable + "\n"));
        
        
        
        System.out.println("\n\n");
        int groupId = 0;
        System.out.println("Day Timetable:\ngroupId = " + groupId + "; day = " + day + "\n");
        output = service.getDayTimetableForGroup(groupId, day);
        output.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + "\n" + entry.getValue());
        });
        
        System.out.println("\nWeek Timetable:\ngroupId = " + groupId + "\n");
        timetable = service.getWeekTimetableForGroup(groupId);
        map = timetable.getValue();
        map.forEach((dayy, dayTimetable) -> System.out.println("day = " + dayy + "\nday timetable:\n" + dayTimetable + "\n"));
        context.close();
    }
}
