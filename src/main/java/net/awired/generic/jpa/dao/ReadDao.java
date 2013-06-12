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
package net.awired.generic.jpa.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.generic.jpa.entity.IdEntity;

public interface ReadDao<ENTITY extends IdEntity<KEY>, KEY extends Serializable> {

    ENTITY find(KEY id) throws NotFoundException;

    List<ENTITY> find(KEY[] ids);

    List<ENTITY> find(Collection<KEY> ids);

    List<ENTITY> findAll();

    void refresh(ENTITY entity);

}
