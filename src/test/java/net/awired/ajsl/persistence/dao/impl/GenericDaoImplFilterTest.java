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
package net.awired.ajsl.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import net.awired.ajsl.persistence.dao.EntityTest;
import net.awired.ajsl.persistence.dao.EntityTestBuilder;
import net.awired.generic.jpa.dao.impl.GenericDaoImpl;
import net.awired.generic.jpa.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@Transactional
public class GenericDaoImplFilterTest extends GenericDaoImpl<EntityTest, Long> {

    @Test
    public void should_find_without_filter() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());

        List<EntityTest> findFiltered = findFiltered(null, null, null, null, null);

        assertEquals(3, findFiltered.size());
    }

    @Test
    public void should_order_by_asc() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());

        List<EntityTest> findFiltered = findFiltered(null, null, null, null, Arrays.asList(new Order("name asc")));

        assertEquals(3, findFiltered.size());
        assertEquals("salut", findFiltered.get(0).getName());
        assertEquals("salut2", findFiltered.get(1).getName());
        assertEquals("salut3", findFiltered.get(2).getName());
    }

    @Test
    public void should_order_by_desc() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());

        List<EntityTest> findFiltered = findFiltered(null, null, null, null, Arrays.asList(new Order("name desc")));

        assertEquals(3, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
        assertEquals("salut2", findFiltered.get(1).getName());
        assertEquals("salut", findFiltered.get(2).getName());
    }

    @Test
    public void should_limit_with_max_results() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(2, null, null, null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut", findFiltered.get(0).getName());
        assertEquals("salut2", findFiltered.get(1).getName());
    }

    @Test
    public void should_limit_with_starting() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, 2, null, null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_limit_with_starting_and_max() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(1, 2, null, null, null);

        assertEquals(1, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
    }

    @Test
    public void should_limit_with_starting_max_and_order() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(1, 2, null, null, Arrays.asList(new Order("name desc")));

        assertEquals(1, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
    }

    @Test
    public void should_find_ordered_if_2_order() {
        persist(new EntityTestBuilder().name("a").description("d").build());
        persist(new EntityTestBuilder().name("b").description("c").build());
        persist(new EntityTestBuilder().name("c").description("b").build());
        persist(new EntityTestBuilder().name("c").description("a").build());

        List<EntityTest> findFiltered = findFiltered(4, null, null, null,
                Arrays.asList(new Order("name desc"), new Order("description desc")));

        assertEquals(4, findFiltered.size());
        assertEquals("b", findFiltered.get(0).getDescription());
        assertEquals("a", findFiltered.get(1).getDescription());
        assertEquals("c", findFiltered.get(2).getDescription());
        assertEquals("d", findFiltered.get(3).getDescription());
    }

    @Test
    public void should_find_ordered_if_2_order_asc() {
        persist(new EntityTestBuilder().name("a").description("d").build());
        persist(new EntityTestBuilder().name("b").description("c").build());
        persist(new EntityTestBuilder().name("c").description("b").build());
        persist(new EntityTestBuilder().name("c").description("a").build());

        List<EntityTest> findFiltered = findFiltered(4, null, null, null,
                Arrays.asList(new Order("name asc"), new Order("description asc")));

        assertEquals(4, findFiltered.size());
        assertEquals("d", findFiltered.get(0).getDescription());
        assertEquals("c", findFiltered.get(1).getDescription());
        assertEquals("a", findFiltered.get(2).getDescription());
        assertEquals("b", findFiltered.get(3).getDescription());
    }

    @Test
    public void should_find_with_search() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "t2", null, null);

        assertEquals(1, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
    }

    @Test
    public void should_find_with_search_unsensitive() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "T2", null, null);

        assertEquals(1, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
    }

    @Test
    public void should_find_with_search_trim() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, " T2 ", null, null);

        assertEquals(1, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
    }

    @Test
    public void should_find_with_search_multi() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "T4, T2", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").description("same as salut4 on search").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "T4", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_integer() {
        persist(new EntityTestBuilder().name("salut").build());
        persist(new EntityTestBuilder().name("salut2").age(4).build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "4", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut2", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_primitive() {
        persist(new EntityTestBuilder().name("salut").cars(4).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "4", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_primitive_long() {
        persist(new EntityTestBuilder().name("salut").longVal(3).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "3", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut", findFiltered.get(0).getName());
        assertEquals("salut3", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_only_listed() {
        persist(new EntityTestBuilder().name("salut").longVal(3).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "3", Arrays.asList("name"), null);

        assertEquals(1, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_only_listed_specific_field() {
        persist(new EntityTestBuilder().name("salut").longVal(3).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "name:3, 4", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_with_search_multi_properties_only_listed_specific_field_trim() {
        persist(new EntityTestBuilder().name("salut").longVal(3).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        List<EntityTest> findFiltered = findFiltered(null, null, "4 , name: 3 , 4", null, null);

        assertEquals(2, findFiltered.size());
        assertEquals("salut3", findFiltered.get(0).getName());
        assertEquals("salut4", findFiltered.get(1).getName());
    }

    @Test
    public void should_find_filtered_count() {
        persist(new EntityTestBuilder().name("salut").longVal(3).build());
        persist(new EntityTestBuilder().name("salut2").build());
        persist(new EntityTestBuilder().name("salut3").build());
        persist(new EntityTestBuilder().name("salut4").build());

        Long count = findFilteredCount("name:3, 4", null);

        assertEquals((Long) 2L, count);
    }

}
