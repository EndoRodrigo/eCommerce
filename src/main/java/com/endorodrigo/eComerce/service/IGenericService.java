package com.endorodrigo.eComerce.service;

import java.util.List;

public interface IGenericService<T, ID> {
    T findId(ID id);
    List<T> getAll();
    T insert(T entity);
    void delete(T entity);
}
