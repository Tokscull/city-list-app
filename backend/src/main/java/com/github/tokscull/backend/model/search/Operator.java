package com.github.tokscull.backend.model.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public enum Operator {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, Filter request, Predicate predicate) {
            Expression<?> key = root.get(request.getField());
            return cb.and(cb.equal(key, request.getValue().toString()), predicate);
        }
    },

    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, Filter request, Predicate predicate) {
            Expression<?> key = root.get(request.getField());
            return cb.and(cb.notEqual(key, request.getValue().toString()), predicate);
        }
    },

    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, Filter request, Predicate predicate) {
            Expression<String> key = root.get(request.getField());
            return cb.and(cb.like(key, "%" + request.getValue().toString() + "%"), predicate);
        }
    };

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, Filter request, Predicate predicate);
}
