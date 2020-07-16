package ua.com.foxminded.university.spring.dao;

import java.util.List;

import ua.com.foxminded.university.spring.dao.exception.DatabaseException;
import ua.com.foxminded.university.spring.dao.exception.ObjectNotFoundException;

public interface Dao<T> {
   
    T getById(int id) throws ObjectNotFoundException;
    
    List<T> getAll() throws ObjectNotFoundException;
     
    boolean delete(T t) throws DatabaseException;
     
    boolean update(T t) throws DatabaseException;
     
    boolean create(T t) throws DatabaseException;
    
}
