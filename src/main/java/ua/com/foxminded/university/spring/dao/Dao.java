package ua.com.foxminded.university.spring.dao;

import java.util.List;

public interface Dao<T> {
   
    T getById(int id);
    
    List<T> getAll();
     
    boolean delete(T t);
     
    boolean update(T t);
     
    boolean create(T t);
    
}
