package net.ogorkis.data;

import net.ogorkis.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class UserRepo {

    @Inject
    private EntityManager em;

    public User save(User user) {
        return em.merge(user);
    }

    public User find(Long userId) {
        return em.find(User.class, userId);
    }

    public User findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

}
