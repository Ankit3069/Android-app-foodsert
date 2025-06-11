package com.example.personalizedecommerceapp.interfaces;

import java.util.List;

public interface IController<T> {

    long save(T entity);
    long update(T entity);
    boolean delete(T entity);
    List<T> getAll();
    T getById(String id);
    List<T> getByCondition(String whereClause,String[] clauseValue);
    List<T> getByQuery(String query);

 }
