package org.core.dao.UserDaoAbstraction;

public interface UserDao<T> {
    public void addUser(T user);
    public void updateUser(T user,String updatedName);
    public T selectUser(int id);
}
