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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;

public abstract class HibernateEnumAdapter<T extends Enum<T> & Identified> implements EnhancedUserType {

    private static final int[] SQL_TYPES = { Types.NUMERIC };

    private Class<T> clazz;

    public HibernateEnumAdapter(Class<T> c) {
        this.clazz = c;
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<T> returnedClass() {
        return clazz;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException,
            SQLException {
        Integer id = resultSet.getInt(names[0]);
        T result = null;
        if (!resultSet.wasNull()) {
            result = findEnumValue(id);
        }
        return result;
    }

    private T findEnumValue(Integer id) {
        for (T enumValue : values()) {
            if (enumValue.getId().equals(id)) {
                return enumValue;
            }
        }
        throw new HibernateException("Cannot find '" + clazz.getName() + "' with id '" + id + "'");
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException,
            SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.NUMERIC);
        } else {
            preparedStatement.setInt(index, ((T) value).getId());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public String objectToSQLString(Object value) {
        int ordinal = ((T) value).getId();
        return Integer.toString(ordinal);
    }

    @Override
    public String toXMLString(Object value) {
        int ordinal = ((T) value).getId();
        return Integer.toString(ordinal);
    }

    @Override
    public Object fromXMLString(String xmlValue) {
        try {
            int ordinal = Integer.parseInt(xmlValue);
            return findEnumValue(ordinal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unknown value for enum " + clazz + ": " + xmlValue);
        }
    }

    public abstract T[] values();

}
