package org.spring1.repository;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.spring1.entity.Task;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TaskRepo {
    private final SessionFactory sessionFactory;

    public TaskRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Task getById(int id){
        Query<Task> query = getCurrentSession().createQuery("select t from Task t  where t.id = :ID", Task.class);
        query.setParameter("ID", id);
        return query.uniqueResult();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAllTasks(int offset, int count){
        Query<Task> query = getCurrentSession().createQuery("select t from Task t", Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(count);

        return query.getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getAllCount(){
        Query<Long> query = getCurrentSession().createQuery("select count(t) from Task t", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(final Task task){
        getCurrentSession().persist(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Task task){
        getCurrentSession().remove(task);
    }

    protected Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }
}
