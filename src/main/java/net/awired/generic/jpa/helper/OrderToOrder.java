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
package net.awired.generic.jpa.helper;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class OrderToOrder {

    public static List<Order> toJpa(CriteriaBuilder cb, Root<?> p,
            List<net.awired.generic.jpa.entity.Order> orders) {
        ArrayList<Order> arrayList = new ArrayList<Order>(orders.size());
        for (net.awired.generic.jpa.entity.Order order : orders) {
            arrayList.add(toJpa(cb, p, order));
        }
        return arrayList;
    }

    public static Order toJpa(CriteriaBuilder cb, Root<?> p, net.awired.generic.jpa.entity.Order order) {
        if (order.isAscending()) {
            return cb.asc(p.get(order.getProperty()));
        }
        return cb.desc(p.get(order.getProperty()));
    }

}
