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
public class AdvancedGenericDaoImplTest extends GenericDaoImpl<EntityTest, Long> {

    @Test
    public void should_find_by_flags() {
        //        EntityTest entity = new EntityTest();
        //        entity.setName("entity1");
        //        save(entity);
        //        EntityTest entity2 = new EntityTest();
        //        entity.setName("entity2");
        //        entity.setSimpleProperty(true);
        //        save(entity2);
        //
        //        List<EntityTest> find = find(Arrays.asList(new Long[] { entity2.getId(), entity.getId() }),
        //                TestFlagProperty.class);
        //
        //        Assert.assertEquals(1, find.size());
        //        Assert.assertEquals("entity2", find.get(0).getName());
    }

}
