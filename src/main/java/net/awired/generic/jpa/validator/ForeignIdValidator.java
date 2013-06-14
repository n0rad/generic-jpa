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
package net.awired.generic.jpa.validator;

import java.io.Serializable;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.generic.jpa.dao.ReadDao;
import net.awired.generic.jpa.entity.IdEntity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ForeignIdValidator implements ConstraintValidator<ForeignId, Serializable>, ApplicationContextAware {

    @Inject
    private ApplicationContext applicationContext;
    private ReadDao<IdEntity<Serializable>, Serializable> dao;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(ForeignId constraintAnnotation) {
        try {
            dao = (ReadDao<IdEntity<Serializable>, Serializable>) applicationContext.getBean(constraintAnnotation
                    .daoName());
        } catch (Exception e) {
            throw new IllegalStateException("cannot found (or invalid) dao named : " + constraintAnnotation.daoName());
        }
    }

    @Override
    public boolean isValid(Serializable id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) {
            return true;
        }
        try {
            IdEntity<Serializable> find = dao.find(id);
            return find != null;
        } catch (NotFoundException e) {
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
