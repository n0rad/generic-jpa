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
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import com.google.common.base.Objects;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class NestedSetEntityImpl<KEY_TYPE extends Serializable> implements NestedSet<KEY_TYPE> {

    @Id
    @GeneratedValue
    protected KEY_TYPE id;

    @Column(nullable = true)
    protected KEY_TYPE parentId;
    protected KEY_TYPE threadId; // null on root
    protected Long left;
    protected Long right;

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("id", id) //
                .add("threadId", threadId) //
                .add("parentId", parentId) //
                .add("left", left) //
                .add("right", right) //
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NestedSetEntityImpl) {
            @SuppressWarnings("unchecked")
            NestedSetEntityImpl<KEY_TYPE> other = (NestedSetEntityImpl<KEY_TYPE>) obj;
            return Objects.equal(id, other.id);
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Override
    public KEY_TYPE getId() {
        return id;
    }

    @Override
    public void setId(KEY_TYPE id) {
        this.id = id;
    }

    @Override
    public KEY_TYPE getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(KEY_TYPE parentId) {
        this.parentId = parentId;
    }

    @Override
    public KEY_TYPE getThreadId() {
        return threadId;
    }

    @Override
    public void setThreadId(KEY_TYPE threadId) {
        this.threadId = threadId;
    }

    @Override
    @XmlElement
    public Long getLeft() {
        return left;
    }

    @Override
    public void setLeft(Long left) {
        this.left = left;
    }

    @Override
    @XmlElement
    public Long getRight() {
        return right;
    }

    @Override
    public void setRight(Long right) {
        this.right = right;
    }

}
