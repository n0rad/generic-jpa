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
import java.util.List;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.generic.jpa.dao.NestedSetDao;
import net.awired.generic.jpa.entity.NestedSet;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.ImmutableList;

@NamedQuery(name = "findByThreadId", query = "FROM CITY c WHERE name = :CityName")
@Transactional(propagation = Propagation.SUPPORTS)
public abstract class NestedSetDaoImpl<ENTITY extends NestedSet<KEY>, KEY extends Serializable> extends
        AdvancedDaoImpl<ENTITY, KEY> implements NestedSetDao<ENTITY, KEY> {

    public NestedSetDaoImpl() {
        super();
    }

    public NestedSetDaoImpl(Class<ENTITY> entityClass, Class<KEY> keyClass) {
        super(entityClass, keyClass);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(ENTITY object) {

        if (object.getParentId() != null) {
            try {
                ENTITY parent = find(object.getParentId());
                List<ENTITY> entities = findByThreadId(parent.getThreadId());
                entities.add(object);
                rebuildLeftRightThreadBasedOnParent(entities);
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            super.persist(object);
            rebuildLeftRightThreadBasedOnParent(ImmutableList.of(object));
        }
        super.persist(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ENTITY save(ENTITY object) {
        if (object.getId() == null) {
            persist(object);
            return object;
        }
        return merge(object);
    }

    @Override
    public List<ENTITY> findByThreadId(KEY threadId) {
        TypedQuery<ENTITY> query = entityManager.createQuery("select r from " + entityClass.getName()
                + " r where (r.threadId = :threadId or r.id = :threadId)", entityClass);
        query.setParameter("threadId", threadId);
        return findList(query);
    }

    public List<ENTITY> findByLeftRight(KEY threadId, long left, long right) {
        //        Object[] val = { idThread, idThread, left, right };
        return null;
        //      List<ENTITY> l = this.getHibernateTemplate().find(
        //      "select r from " + ReflectionToolrs.getGenericClass(this, 0).getName()
        //              + " r where (r.nsThread.id = ? or r.id = ?) and r.nsLeft >= ? and r.nsRight <= ?", val);
    }

    private void rebuildLeftRightThreadBasedOnParent(List<ENTITY> entities) {
        ENTITY root = findRootFromList(entities);
        root.setLeft(0L);
        root.setThreadId(root.getId());
        long current = rebuildLeftRightThreadBasedOnParentRec(entities, root.getId(), root, 1);
        root.setRight(current);
    }

    private long rebuildLeftRightThreadBasedOnParentRec(List<ENTITY> entities, KEY threadId, ENTITY parent,
            long current) {
        long tmpCurrent = current;
        for (ENTITY entity : entities) {
            if (entity.getParentId() != null && entity.getParentId().equals(parent.getId())) {
                entity.setThreadId(threadId);
                entity.setLeft(tmpCurrent++);
                tmpCurrent = rebuildLeftRightThreadBasedOnParentRec(entities, threadId, entity, tmpCurrent);
                entity.setRight(tmpCurrent++);
            }

        }
        return tmpCurrent;
    }

    private ENTITY findRootFromList(List<ENTITY> entities) {
        for (ENTITY entity : entities) {
            if (entity.getParentId() == null || entity.getId() == entity.getParentId()) {
                return entity;
            }
        }
        return null;
    }

}
