/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package net.awired.generic.jpa.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.core.lang.reflect.ReflectTools;
import net.awired.generic.jpa.dao.CrudDao;
import net.awired.generic.jpa.entity.IdEntity;
import net.awired.generic.jpa.entity.Order;
import net.awired.generic.jpa.helper.OrderToOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Strings;

@Transactional(propagation = Propagation.SUPPORTS)
public class GenericDaoImpl<ENTITY extends IdEntity<KEY>, KEY extends Serializable> implements CrudDao<ENTITY, KEY> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<ENTITY> entityClass;
    protected Class<KEY> keyClass;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        entityClass = (Class<ENTITY>) ReflectTools.getClassDeclaredInSuperClassGeneric(this, 0);
        keyClass = (Class<KEY>) ReflectTools.getClassDeclaredInSuperClassGeneric(this, 1);
        log.debug("Creation of dao for entity '{}' and key type '{}'", entityClass, keyClass);
    }

    public GenericDaoImpl(Class<ENTITY> entityClass, Class<KEY> keyClass) {
        this.entityClass = entityClass;
        this.keyClass = keyClass;
        log.debug("Creation of dao for entity '{}' and key type '{}'", entityClass, keyClass);
    }

    //////////////////
    //////////////////

    /**
     * @param length
     *            maxResult
     * @param start
     *            pagination starting point
     * @param search
     *            search string like ' name: toto , dupont' to search for %toto% on name + %dupont% in all properties
     * @param searchProperties
     *            properties to search or all if null
     * @param orders
     *            sorted result by properties
     * @return
     */
    public List<ENTITY> findFiltered(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = builder.createQuery(entityClass);
        Root<ENTITY> root = query.from(entityClass);

        if (!Strings.isNullOrEmpty(search)) {
            Predicate[] buildFilterPredicates = BuildFilterPredicates(root, search, searchProperties);
            if (buildFilterPredicates.length > 0) {
                query.where(builder.or(buildFilterPredicates));
            }
        }

        if (orders != null) {
            query.orderBy(OrderToOrder.toJpa(builder, root, orders));
        }

        TypedQuery<ENTITY> q = entityManager.createQuery(query);
        if (start != null) {
            q.setFirstResult(start);
        }
        if (length != null) {
            q.setMaxResults(length);
        }
        return findList(q);
    }

    public Long findFilteredCount(String search, List<String> searchProperties) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<ENTITY> from = query.from(entityClass);
        query.select(builder.count(from));
        if (!Strings.isNullOrEmpty(search)) {
            Predicate[] buildFilterPredicates = BuildFilterPredicates(from, search, searchProperties);
            if (buildFilterPredicates.length > 0) {
                query.where(builder.or(buildFilterPredicates));
            }
        }
        return count(entityManager.createQuery(query));
    }

    private Predicate[] BuildFilterPredicates(Root<ENTITY> root, String search, List<String> searchProperties) {
        if (Strings.isNullOrEmpty(search)) {
            return new Predicate[] {};
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        EntityType<ENTITY> type = entityManager.getMetamodel().entity(entityClass);

        String[] split = search.split(",");

        Set<SingularAttribute<? super ENTITY, ?>> attributes = type.getSingularAttributes();
        List<Predicate> predicates = new ArrayList<Predicate>(split.length * attributes.size());
        for (String searchElem : split) {
            String searchProperty = null;
            if (searchElem.contains(":")) {
                String[] propSearchs = searchElem.trim().split(":", 2);
                searchElem = propSearchs[1];
                searchProperty = propSearchs[0];
            }

            boolean numeric;
            try {
                Double.parseDouble(searchElem);
                numeric = true;
            } catch (Exception e) {
                numeric = false;
            }
            for (SingularAttribute<? super ENTITY, ?> attribute : attributes) {
                if (searchProperties != null && !searchProperties.isEmpty()
                        && !searchProperties.contains(attribute.getName())) {
                    continue; // skip this property as its not listed in searchable properties
                }
                if (searchProperty != null && !searchProperty.equals(attribute.getName())) {
                    continue; // skip this property as we are searching for specific property
                }
                Class<?> javaType = attribute.getJavaType();
                if (javaType == String.class) {
                    @SuppressWarnings("unchecked")
                    Predicate like = builder.like(
                            builder.lower(root.get((SingularAttribute<ENTITY, String>) attribute)), "%"
                                    + searchElem.toLowerCase().trim() + "%");
                    predicates.add(like);
                } else if (numeric
                        && (Number.class.isAssignableFrom(javaType) || javaType == int.class
                                || javaType == short.class || javaType == long.class || javaType == float.class
                                || javaType == double.class || javaType == byte.class)) {
                    Predicate like = builder.equal(root.get(attribute), searchElem.toLowerCase().trim());
                    predicates.add(like);
                }
                //TODO fancy types
                // enums
                // char   
                // boolean   
            }
        }
        return predicates.toArray(new Predicate[] {});
    }

    @Override
    public ENTITY find(KEY id) throws NotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("Finding by id " + entityClass + "#" + id);
        }
        ENTITY entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new NotFoundException("entity " + entityClass + "#" + id + " not found");
        }
        return entity;
    }

    @Override
    public List<ENTITY> find(Collection<KEY> ids) {
        if (log.isDebugEnabled()) {
            log.debug("Finding by ids " + entityClass + "#" + ids);
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ENTITY> criteriaQuery = cb.createQuery(entityClass);
        Root<ENTITY> from = criteriaQuery.from(entityClass);
        criteriaQuery.where(from.in(ids));
        TypedQuery<ENTITY> query = entityManager.createQuery(criteriaQuery);
        return findList(query);
    }

    @Override
    public List<ENTITY> find(KEY[] ids) {
        return find(Arrays.asList(ids));
    }

    @Override
    public List<ENTITY> findAll() {
        if (log.isDebugEnabled()) {
            log.debug("Finding all " + entityClass);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ENTITY> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        criteriaQuery.from(entityClass);
        TypedQuery<ENTITY> query = entityManager.createQuery(criteriaQuery);
        return findList(query);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(ENTITY object) {
        if (log.isDebugEnabled()) {
            log.debug("Persisting " + object);
        }
        entityManager.persist(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ENTITY merge(ENTITY object) {
        if (log.isDebugEnabled()) {
            log.debug("Merging " + object);
        }
        return entityManager.merge(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ENTITY save(ENTITY object) {
        if (log.isDebugEnabled()) {
            log.debug("Saving " + object);
        }
        if (object.getId() == null) {
            entityManager.persist(object);
            return object;
        }
        return entityManager.merge(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Collection<ENTITY> objects) {
        if (log.isDebugEnabled()) {
            log.debug("Saving objects " + objects);
        }
        for (ENTITY object : objects) {
            save(object);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(final ENTITY... objects) {
        save(Arrays.asList(objects));
    }

    protected List<ENTITY> findByCriteria(CriteriaQuery<ENTITY> criteriaQuery) {
        if (log.isDebugEnabled()) {
            log.debug("Finding by criteria " + criteriaQuery);
        }
        return findList(entityManager.createQuery(criteriaQuery));
    }

    protected ENTITY findUniqueByCriteria(CriteriaQuery<ENTITY> criteriaQuery) throws NotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("Finding single result by criteria " + criteriaQuery);
        }
        return findSingleResult(entityManager.createQuery(criteriaQuery));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @SuppressWarnings("unchecked")
    public void delete(KEY id) {
        try {
            delete(find(id));
        } catch (NotFoundException e) {
            log.info("Entity not found when trying to delete : " + entityClass + "#" + id);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAll() {
        delete(findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(KEY... ids) {
        delete(find(ids));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(ENTITY... entities) {
        delete(Arrays.asList(entities));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Collection<ENTITY> entities) {
        for (ENTITY entity : entities) {
            if (log.isDebugEnabled()) {
                log.debug("Deleting entity : " + entity);
            }
            entityManager.remove(entity);
        }
    }

    @Override
    public void refresh(ENTITY entity) {
        entityManager.refresh(entity);
    }

    ///////////////////////////////////////

    @Transactional(propagation = Propagation.SUPPORTS)
    protected ENTITY findSingleResult(TypedQuery<ENTITY> query) throws NotFoundException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Object '" + entityClass + "' with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected <E> E findGenericSingleResult(TypedQuery<E> query) throws NotFoundException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Result of " + query + " with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected Object findSingleResult(Query query) throws NotFoundException {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Result of " + query + " with parameters '"
                    + Arrays.toString(query.getParameters().toArray()) + "' not found in database", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS)
    protected List<Object[]> findMultiResultList(Query query) {
        List<Object[]> result = query.getResultList();
        if (result == null) {
            result = new ArrayList<Object[]>();
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected <U> List<U> findList(TypedQuery<U> query) {
        List<U> result = query.getResultList();
        if (result == null) {
            result = new ArrayList<U>();
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected long count(TypedQuery<Long> query) {
        return query.getSingleResult();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void update(Query query) {
        query.executeUpdate();
    }

}
