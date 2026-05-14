package org.core.dao.UserDaoAbstraction;

public interface UserDao<T> {
    public void add(T user);
    public void update(T user, String updatedName);
    public T select(int id);
}
