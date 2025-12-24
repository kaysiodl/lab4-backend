package com.kaysiodl.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<Result> findByUserPaged(User user,
                                        int page,
                                        int size,
                                        String sortField,
                                        String sortDir,
                                        Map<String, Map<String, String>> filters
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Result> cq = cb.createQuery(Result.class);
        Root<Result> root = cq.from(Result.class);
        List<Predicate> predicates = buildPredicates(cb, root, user, filters);

        cq.where(predicates.toArray(new Predicate[0]));

        if (sortField != null) {
            cq.orderBy(
                    "desc".equalsIgnoreCase(sortDir)
                            ? cb.desc(root.get(sortField))
                            : cb.asc(root.get(sortField))
            );
        }

        TypedQuery<Result> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countByUser(User user,
                            Map<String, Map<String, String>> filters
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Result> root = cq.from(Result.class);

        List<Predicate> predicates = buildPredicates(cb, root, user, filters);

        cq.select(cb.count(root));
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getSingleResult();
    }


    @Transactional
    public void deleteAllByUser(User user) {
        entityManager.createQuery("DELETE FROM Result r WHERE r.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Result> root,
            User user,
            Map<String, Map<String, String>> filters
    ) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("user"), user));

        for (var entry : filters.entrySet()) {
            String field = entry.getKey();
            Map<String, String> ops = entry.getValue();

            for (var opEntry : ops.entrySet()) {
                String op = opEntry.getKey();
                String value = opEntry.getValue();

                Predicate predicate = switch (op) {
                    case "eq" -> cb.equal(
                            root.get(field),
                            cast(root, field, value)
                    );
                    case "gt" -> cb.greaterThan(
                            root.get(field),
                            (Comparable) cast(root, field, value)
                    );
                    case "lt" -> cb.lessThan(
                            root.get(field),
                            (Comparable) cast(root, field, value)
                    );
                    default -> null;
                };

                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }

        return predicates;
    }


    private Object cast(Root<Result> root, String field, String value) {
        Class<?> type = root.get(field).getJavaType();

        if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.valueOf(value);
        }
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.valueOf(value);
        }
        if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.valueOf(value);
        }
        return value;
    }


}
