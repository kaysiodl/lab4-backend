package com.kaysiodl.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ResultsRepository {
    @PersistenceContext(name = "pg")
    private EntityManager entityManager;

    @Transactional
    public void add(Result result) {
        entityManager.persist(result);
    }

    public List<Result> getAll() {
        return entityManager.createQuery("SELECT r FROM Result r ORDER BY r.id", Result.class)
                .getResultList();
    }

    public List<Result> findByUser(User user) {
        return entityManager.createQuery(
                        "SELECT r FROM Result r WHERE r.user = :user ORDER BY r.id DESC",
                        Result.class
                )
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public void deleteAllByUser(User user){
        entityManager.createQuery("DELETE FROM Result r WHERE r.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

}
