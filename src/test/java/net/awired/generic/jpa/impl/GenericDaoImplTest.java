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

import java.util.List;
import junit.framework.Assert;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.generic.jpa.EntityTest;
import net.awired.generic.jpa.dao.impl.GenericDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@Transactional
public class GenericDaoImplTest extends GenericDaoImpl<EntityTest, Long> {

    @Test
    public void should_add_entity() {
        EntityTest entity = new EntityTest();
        entity.setName("genre");
        EntityTest persisted = save(entity);
        Assert.assertEquals("genre", persisted.getName());
        Assert.assertNotNull(persisted.getId());
    }

    @Test
    public void should_update_entity() throws Exception {
        EntityTest entity = new EntityTest();
        entity.setName("genre2");
        EntityTest persisted = save(entity);
        persisted.setName("newgenre");
        save(persisted);

        EntityTest res = find(persisted.getId());
        Assert.assertEquals("newgenre", res.getName());
    }

    @Test(expected = NotFoundException.class)
    public void should_not_found_entity() throws Exception {
        find(4242424242L);
    }

    @Test
    public void should_find_by_id_list() {
        EntityTest entity = new EntityTest();
        entity.setName("entity1");
        save(entity);
        EntityTest entity2 = new EntityTest();
        entity.setName("entity2");
        save(entity2);

        List<EntityTest> find = find(new Long[] { entity2.getId(), entity.getId() });

        Assert.assertEquals(2, find.size());
    }

}
