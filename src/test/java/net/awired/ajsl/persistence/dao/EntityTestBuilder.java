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
package net.awired.ajsl.persistence.dao;

public class EntityTestBuilder {

    private String name;
    private String description;
    private boolean simpleProperty;
    private Integer age;
    private Integer cars;
    private int longVal;

    public EntityTest build() {
        EntityTest entityTest = new EntityTest();
        entityTest.setName(name);
        entityTest.setSimpleProperty(simpleProperty);
        entityTest.setDescription(description);
        entityTest.setAge(age);
        if (cars != null) {
            entityTest.setCars(cars);
        }
        entityTest.setLongVal(longVal);
        return entityTest;
    }

    public EntityTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EntityTestBuilder simpleProperty(boolean simpleProperty) {
        this.simpleProperty = simpleProperty;
        return this;
    }

    public EntityTestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public EntityTestBuilder age(int age) {
        this.age = age;
        return this;
    }

    public EntityTestBuilder cars(int cars) {
        this.cars = cars;
        return this;
    }

    public EntityTestBuilder longVal(int i) {
        this.longVal = i;
        return this;
    }

}
