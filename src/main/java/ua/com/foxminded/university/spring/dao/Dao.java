package ua.com.foxminded.university.spring.dao;

import java.util.List;

public interface Dao<T> {
   
    T getById(int id);
    
    List<T> getAll();
     
    boolean delete(T t);
     
    boolean update(T t); //void update(T t, String[] params);
     
    boolean create(T t);
    
}
