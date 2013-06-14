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
package net.awired.generic.jpa.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import javax.persistence.Entity;
import net.awired.generic.jpa.dao.impl.NestedSetDaoImpl;
import net.awired.generic.jpa.entity.NestedSetEntityImpl;
import net.awired.generic.jpa.impl.NestedSetDaoImplTest.NestedEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@Transactional
public class NestedSetDaoImplTest extends NestedSetDaoImpl<NestedEntity, Long> {

    @Entity
    public class NestedEntity extends NestedSetEntityImpl<Long> {
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void should_create_single_node() throws Exception {
        NestedEntity entity = new NestedEntity();
        entity.setName("genre2");

        persist(entity);

        assertNotNull(entity.getId());
        assertEquals(entity.getId(), entity.getThreadId());
        assertEquals((Long) 0L, entity.getLeft());
        assertEquals((Long) 1L, entity.getRight());
    }

    @Test
    public void should_create_single_node2() throws Exception {
        NestedEntity entity = new NestedEntity();
        entity.setName("genre2");

        persist(entity);

        NestedEntity find = find(entity.getId());

        assertNotNull(entity.getId());
        assertEquals(entity.getId(), find.getThreadId());
        assertEquals((Long) 0L, find.getLeft());
        assertEquals((Long) 1L, find.getRight());
    }

    @Test
    public void should_create_single_node_on_save() throws Exception {
        NestedEntity entity = new NestedEntity();
        entity.setName("genre2");

        save(entity);

        assertNotNull(entity.getId());
        assertEquals(entity.getId(), entity.getThreadId());
        assertEquals((Long) 0L, entity.getLeft());
        assertEquals((Long) 1L, entity.getRight());
    }

    @Test
    public void should_create_second_node() throws Exception {
        NestedEntity entity = new NestedEntity();
        entity.setName("root");
        save(entity);
        NestedEntity entity2 = new NestedEntity();
        entity2.setName("node2");
        entity2.setParentId(entity.getId());

        save(entity2);

        assertNotNull(entity.getId());
        assertEquals(entity.getId(), entity.getThreadId());
        assertEquals((Long) 0L, entity.getLeft());
        assertEquals((Long) 3L, entity.getRight());

        assertNotNull(entity2.getId());
        assertEquals(entity.getId(), entity2.getThreadId());
        assertEquals((Long) 1L, entity2.getLeft());
        assertEquals((Long) 2L, entity2.getRight());
    }

    @Test
    public void should_find_by_thread() throws Exception {
        NestedEntity entity = new NestedEntity();
        entity.setName("root");
        save(entity);
        NestedEntity entity2 = new NestedEntity();
        entity2.setName("node2");
        entity2.setParentId(entity.getId());
        save(entity2);

        List<NestedEntity> entities = findByThreadId(entity.getId());

        assertNotNull(entities.get(0).getId());
        assertEquals(entities.get(0).getId(), entities.get(0).getThreadId());
        assertEquals((Long) 0L, entities.get(0).getLeft());
        assertEquals((Long) 3L, entities.get(0).getRight());

        assertNotNull(entities.get(1).getId());
        assertEquals(entities.get(0).getId(), entities.get(1).getThreadId());
        assertEquals((Long) 1L, entities.get(1).getLeft());
        assertEquals((Long) 2L, entities.get(1).getRight());

    }
}
