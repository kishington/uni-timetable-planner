package ua.com.foxminded.university.spring.dao;

import java.util.List;

import ua.com.foxminded.university.spring.dao.exception.DatabaseException;

public interface Dao<T> {
   
    T getById(int id) throws DatabaseException;
    
    List<T> getAll() throws DatabaseException;
     
    boolean delete(T t) throws DatabaseException;
     
    boolean update(T t) throws DatabaseException;
     
    boolean create(T t) throws DatabaseException;
    
}
