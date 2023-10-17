package web.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<User> getAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getById(int id) {
        return entityManager.createQuery("select u from User u where u.id=:id", User.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User updatedUser) {
        entityManager.merge(updatedUser);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }
}
