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
package net.awired.generic.jpa.entity;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface NestedSet<KEY_TYPE extends Serializable> extends IdEntity<KEY_TYPE> {

    KEY_TYPE getParentId();

    void setParentId(KEY_TYPE parentId);

    KEY_TYPE getThreadId();

    void setThreadId(KEY_TYPE threadId);

    Long getLeft();

    void setLeft(Long left);

    Long getRight();

    void setRight(Long right);

}
