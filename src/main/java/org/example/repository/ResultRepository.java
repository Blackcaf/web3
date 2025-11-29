package org.example.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entities.ResultEntity;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ResultRepository {

    private static final String PERSISTENCE_UNIT = "web3PU";

    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        System.out.println("ResultRepository: Initialized");
    }

    @PreDestroy
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("ResultRepository: Destroyed");
        }
    }

    public ResultEntity save(ResultEntity entity) {
        return executeInTransaction(em -> {
            em.persist(entity);
            em.flush();
            return entity;
        });
    }

    public List<ResultEntity> findAll() {
        return executeReadOnly(em ->
                em.createQuery("SELECT r FROM ResultEntity r ORDER BY r.timestamp DESC", ResultEntity.class)
                        .getResultList()
        );
    }

    public List<ResultEntity> findAllOrderByIdAsc() {
        return executeReadOnly(em ->
                em.createQuery("SELECT r FROM ResultEntity r ORDER BY r.id ASC", ResultEntity.class)
                        .getResultList()
        );
    }

    public void deleteAll() {
        executeInTransaction(em -> {
            em.createQuery("DELETE FROM ResultEntity").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE results_id_seq RESTART WITH 1").executeUpdate();
            return null;
        });
        System.out.println("ResultRepository: All records deleted, ID sequence reset");
    }

    public long count() {
        return executeReadOnly(em ->
                em.createQuery("SELECT COUNT(r) FROM ResultEntity r", Long.class)
                        .getSingleResult()
        );
    }

    private <T> T executeInTransaction(TransactionCallback<T> callback) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T result = callback.execute(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("ResultRepository: Transaction failed - " + e.getMessage());
            throw new RepositoryException("Database operation failed", e);
        } finally {
            em.close();
        }
    }

    private <T> T executeReadOnly(ReadCallback<T> callback) {
        EntityManager em = emf.createEntityManager();
        try {
            return callback.execute(em);
        } catch (Exception e) {
            System.err.println("ResultRepository: Read operation failed - " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    private interface TransactionCallback<T> {
        T execute(EntityManager em);
    }

    @FunctionalInterface
    private interface ReadCallback<T> {
        T execute(EntityManager em);
    }

    public static class RepositoryException extends RuntimeException {
        public RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}