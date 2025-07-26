package com.endorodrigo.eComerce.service;

import java.util.List;

/**
 * Interfaz genérica para servicios CRUD.
 * Define operaciones básicas para entidades.
 * @param <T> Tipo de entidad
 * @param <ID> Tipo de identificador
 */
public interface IGenericService<T, ID> {
    T findId(ID id);
    List<T> getAll();
    T insert(T entity);
    T update(T entity);
    void delete(T entity);

}
