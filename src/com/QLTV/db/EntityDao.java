/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.db;

import java.util.List;

public abstract class EntityDao<E, K> {

    abstract public void insert(E entity);

    abstract public void update(E entity);

    abstract public void delete(K entity);

    abstract public List<E> selectAll();

    abstract public E select_byID(K key);

    abstract protected List<E> select_by_sql(String sql, Object... args);
}
