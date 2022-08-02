package ru.kata.spring.boot_security.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
public class RoleRepositoryImpl implements RoleRepository{

    private final EntityManager entityManager;

    public RoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(Role role) { entityManager.persist(role); }

    @Override
    public Role findByRoleName(String name) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.name =: name", Role.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}

