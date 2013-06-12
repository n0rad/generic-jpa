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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import com.google.common.base.Objects;

/**
 * @XmlAccessorType is none because jaxb cannot handle Serializable generics
 *                  to serialize id override getId() & setId() with @XmlElement
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class IdEntityImpl<KEY_TYPE extends Serializable> implements IdEntity<KEY_TYPE> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    protected KEY_TYPE id;

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("id", id) //
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IdEntityImpl) {
            @SuppressWarnings("unchecked")
            IdEntityImpl<KEY_TYPE> other = (IdEntityImpl<KEY_TYPE>) obj;
            return Objects.equal(id, other.id);
        }
        return false;
    }

    // ////////////////////////////////////////////////////////////////////////

    @Override
    public KEY_TYPE getId() {
        return id;
    }

    @Override
    public void setId(KEY_TYPE id) {
        this.id = id;
    }
}
