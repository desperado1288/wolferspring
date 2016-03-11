package com.wolferx.wolferspring.repository;

import com.wolferx.wolferspring.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> findByUserId(Long userId) {
        Query query = this.entityManager
            .createQuery("select p from post p where p.user_id = :userId");

        return query.setParameter("userId", userId).getResultList();
    }
}