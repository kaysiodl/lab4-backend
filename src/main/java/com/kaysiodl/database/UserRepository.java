package com.kaysiodl.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository {
    @PersistenceContext(name = "pg")
    private EntityManager entityManager;

    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    public User findByLogin(String login) {
        return (User) entityManager.createQuery("select u from User u where u.login = :login");
    }
}
