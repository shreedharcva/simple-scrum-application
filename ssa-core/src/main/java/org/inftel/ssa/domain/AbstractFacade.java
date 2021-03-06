
package org.inftel.ssa.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author ibaca
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
        // getEntityManager().refresh(entity);
    }

    public T edit(T entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public boolean contains(T entity) {
        return getEntityManager().contains(entity);
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public List<T> findSince(Date date) {
        if (date == null) {
            return findAll();
        }

        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = qb.createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        Expression<Date> column = from.get("updated");
        Expression<Date> literal = qb.literal(date);

        cq.select(from).where(qb.greaterThan(column, literal));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<T> find(Integer startPosition, Integer maxResult, String sortField,
            Boolean ascOrder, Map<String, String> filters) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = qb.createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        addCriterias(sortField, ascOrder, cq, qb, from, filters);
        // select entity from entity...
        CriteriaQuery<T> select = cq.select(from);
        TypedQuery<T> q = getEntityManager().createQuery(select);
        if (startPosition != null) {
            q.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            q.setMaxResults(maxResult);
        }

        return q.getResultList();
    }

    public int count(String sortField, Boolean ascOrder, Map<String, String> filters) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<T> from = cq.from(entityClass);
        addCriterias(null, null, cq, qb, from, filters);
        // select count(entity) from entity...
        CriteriaQuery<Long> select = cq.select(qb.count(from));
        TypedQuery<Long> q = getEntityManager().createQuery(select);
        return q.getSingleResult().intValue();
    }

    private void addCriterias(String sortField, Boolean ascOrder, CriteriaQuery<?> cq,
            CriteriaBuilder qb, Root<T> from, Map<String, String> filters)
            throws NumberFormatException {
        if (sortField != null && sortField.trim().length() > 0) {
            boolean asc = (ascOrder == null) ? true : (ascOrder == Boolean.TRUE) ? true : false;
            cq.orderBy((asc) ? qb.asc(from.get(sortField)) : qb.desc(from.get(sortField)));
        }

        filters = (filters == null) ? Collections.<String, String> emptyMap() : filters;
        List<Predicate> predicates = new ArrayList<Predicate>(filters.size());
        for (String column : filters.keySet()) {
            if (column.endsWith(".id")) {
                Expression<Long> literal = qb.literal(Long.decode(filters.get(column)));
                Predicate predicate = qb.equal(from.<Long> get(column.split("\\.")[0]).get("id"),
                        literal);
                predicates.add(predicate);
            } else {
                System.out.println("filtrando por: " + column + ":" + filters.get(column));
                Expression<String> literal = qb.upper(qb.literal(filters.get(column)));
                Predicate predicate = qb.like(qb.upper(from.<String> get(column)), literal);
                predicates.add(predicate);
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));
    }

    public int count() {
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
