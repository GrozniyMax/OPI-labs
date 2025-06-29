package com.web.primefacesexampletest.database;

import com.web.primefacesexampletest.mbeans.DotCounter;
import com.web.primefacesexampletest.util.MBeanRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;


@ApplicationScoped
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestRepositoryImpl implements RequestsRepository, Serializable {

    @PersistenceContext
    EntityManager entityManager;

    private final DotCounter dotCounterMBean = new DotCounter();

    public void init(@Observes @Initialized(SessionScoped.class) Object unused) {
        MBeanRegistry.registerBean(dotCounterMBean, "dotCounter");
    }

    public void destroy(@Observes @Destroyed(SessionScoped.class) Object unused) {
        MBeanRegistry.unregisterBean(dotCounterMBean);

    }

    @Override
    public Stream<Request> findAll() {
        return entityManager.createQuery("SELECT r FROM Request r", Request.class)
                .getResultStream();
    }


    @Transactional
    @Override
    public void save(Request request) {
        dotCounterMBean.updateAttempt(request.getHit());
        if (request.getId() == null) {
            entityManager.persist(request);
        } else {
            entityManager.merge(request);
        }
    }

    @Override
    public List<Request> findSome(int count) {
        return entityManager.createQuery("SELECT r FROM Request r ORDER BY r.startTime DESC", Request.class)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public Stream<Request> findAllHited() {
        return entityManager.createQuery("SELECT r FROM Request r WHERE r.hit = true", Request.class)
                .getResultStream();
    }

    @Override
    public Stream<Request> findAllHitAndR(Float r) {
        return entityManager.createQuery("SELECT r FROM Request r WHERE r.hit = true AND r.r = :radios", Request.class)
                .setParameter("radios", r)
                .getResultStream();
    }
}
