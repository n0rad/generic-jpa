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
import java.lang.reflect.Method;
import net.awired.generic.jpa.entity.IdEntity;

public class AdvancedDaoImpl<ENTITY extends IdEntity<KEY>, KEY extends Serializable> extends
        GenericDaoImpl<ENTITY, KEY> {

    //    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //    private boolean isDefaultable;
    //
    //    private boolean isActivable;
    //
    //    private boolean isArchivable;
    //
    //    private boolean isInheritable;

    public AdvancedDaoImpl() {
        super();
        //        checkGenericClass();
    }

    public AdvancedDaoImpl(Class<ENTITY> entityClass, Class<KEY> keyClass) {
        super(entityClass, keyClass);
        //        checkGenericClass();
    }

    private String extractBooleanPropertyName(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("is")) {
                String res = Character.toLowerCase(method.getName().charAt(2)) + method.getName().substring(3);
                return res;
            }
        }
        throw new RuntimeException("Getter not found in flag Class " + clazz);
    }

    //    public List<ENTITY> find(Collection<KEY> ids, Class<?>... flags) {
    //        if (log.isDebugEnabled()) {
    //            log.debug("Finding by ids " + entityClass + "#" + ids + " with flags " + flags);
    //        }
    //        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    //        CriteriaQuery<ENTITY> criteriaQuery = cb.createQuery(entityClass);
    //        Root<ENTITY> from = criteriaQuery.from(entityClass);
    //        criteriaQuery.where(from.in(ids));
    //        for (Class<?> clazz : flags) {
    //            if (clazz != null) {
    //                String propertyName = extractBooleanPropertyName(clazz);
    //
    //                Predicate nameEquals = cb.equal(from.get(propertyName), true);
    //                criteriaQuery.where(nameEquals);
    //
    //                //                Metamodel m = entityManager.getMetamodel();
    //                //                EntityType<ENTITY> entityType = m.entity(entityClass);
    //                //                Root<ENTITY> root = criteriaQuery.from(entityClass);
    //                //                criteriaQuery.where(cb.equal(root.get(propertyName), true));
    //            }
    //        }
    //
    //        TypedQuery<ENTITY> query = entityManager.createQuery(criteriaQuery);
    //        return findList(query);
    //    }

    //    @Override
    //    public List<ENTITY> findByExample(ENTITY example) {
    //        Criteria criteria = Criteria.forClass(entityClass);
    //
    //        Field[] fields = example.getClass().getDeclaredFields();
    //
    //        for (Field field : fields) {
    //            if (field.getName().equals(ENTITY.ID)) {
    //                continue;
    //            }
    //            if (field.getName().equals(IActivable.P_IS_ACTIVE)) {
    //                continue;
    //            }
    //            if (field.getName().equals(IDefaultable.P_IS_DEFAULT)) {
    //                continue;
    //            }
    //            if (field.getName().equals(IHiddenable.P_IS_HIDDEN)) {
    //                continue;
    //            }
    //            if (!field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Basic.class)) {
    //                continue;
    //            }
    //
    //            Object value = null;
    //
    //            try {
    //                field.setAccessible(true);
    //                value = field.get(example);
    //            } catch (IllegalArgumentException e) {
    //                continue;
    //            } catch (IllegalAccessException e) {
    //                continue;
    //            }
    //
    //            if (value == null) {
    //                continue;
    //            }
    //
    //            criteria.add(Restrictions.eq(field.getName(), value));
    //        }
    //
    //        if (example instanceof IHiddenable) {
    //            if (((IInheritable) example).getParent() == null) {
    //                criteria.add(Restrictions.isNull(IInheritable.P_PARENT));
    //            } else {
    //                criteria.add(Restrictions.eq(IInheritable.P_PARENT, ((IInheritable) example).getParent()));
    //            }
    //        }
    //
    //        return findByCriteria(criteria);
    //    }
    //

    //    @Override
    //    public void flushAndClear() {
    //        entityManager.flush();
    //        entityManager.clear();
    //    }

    ///////////////
    //////////////

    //    private void checkIfDefault(ENTITY entity) {
    //        if (((IDefaultable) entity).isDefault()) {
    //            throw new UnsupportedOperationException("can't delete default entity " + entityClass + "#"
    //                    + entity.getId());
    //        }
    //    }
    //
    //    @Override
    //    public boolean isActivable() {
    //        return isActivable;
    //    }
    //
    //    @Override
    //    public boolean isDefaultable() {
    //        return isDefaultable;
    //    }
    //
    //    @Override
    //    public boolean isHiddenable() {
    //        return isHiddenable;
    //    }
    //
    //    @Override
    //    public boolean isInheritable() {
    //        return isInheritable;
    //    }
    //
    //    @Override
    //    public void setAsDefault(IDefaultable object) {
    //        if (object.getExample() != null) {
    //            List<ENTITY> objects = findByExample((ENTITY) object.getExample());
    //            for (ENTITY o : objects) {
    //                if (object != o) {
    //                    ((IDefaultable) o).setDefault(false);
    //                    entityManager.merge(o);
    //                }
    //            }
    //        }
    //        object.setDefault(true);
    //
    //        if (((ENTITY) object).getId() != null) {
    //            entityManager.merge(object);
    //        } else {
    //            entityManager.persist(object);
    //        }
    //    }
    //

    //
    //    @Override
    //    public List<ENTITY> find(IInheritable<ENTITY> parent) {
    //        if (parent == null) {
    //            return findByCriteria(Criteria.forClass(entityClass).add(Restrictions.isNull(IInheritable.P_PARENT)));
    //        } else {
    //            return findByCriteria(Criteria.forClass(entityClass).add(Restrictions.eq(IInheritable.P_PARENT, parent)));
    //        }
    //    }
    //

    //    @Transactional(propagation = Propagation.REQUIRED)
    //    private void deleteAll(final Collection<ENTITY> entities, boolean checkIdDefault) {
    //        //        if (checkIdDefault) {
    //        //            if (isDefaultable) {
    //        //                for (ENTITY object : objects) {
    //        //                    checkIfDefault(object);
    //        //                }
    //        //            }
    //        //        }
    //        if (isArchivable) {
    //            for (ENTITY object : entities) {
    //                ((Archivable) object).setArchived(true);
    //                entityManager.merge(object);
    //            }
    //        } else {
    //            for (ENTITY object : entities) {
    //                entityManager.remove(object);
    //            }
    //        }
    //    }
    //

    //    private void checkGenericClass() {
    //        for (Class i : entityClass.getInterfaces()) {
    //            if (i == Defaultable.class) {
    //                isDefaultable = true;
    //            } else if (i == Activable.class) {
    //                isActivable = true;
    //            } else if (i == Archivable.class) {
    //                isArchivable = true;
    //                //            } else if (i == Nested.class) {
    //                //                isInheritable = true;
    //            }
    //        }
    //    }

}
