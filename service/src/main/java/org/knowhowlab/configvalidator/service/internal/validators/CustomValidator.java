/*
 *  Copyright (c) 2009-2016 Dmytro Pishchukhin (http://knowhowlab.org)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.knowhowlab.configvalidator.service.internal.validators;

import org.knowhowlab.configvalidator.api.ConfigurationValidator;
import org.knowhowlab.configvalidator.api.InvalidConfigurationException;
import org.knowhowlab.configvalidator.api.annotations.CustomValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class CustomValidator implements InternalConfigurationValidator<Object, CustomValidation> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomValidator.class);

    @Override
    public Priority getPriorityOrder() {
        return Priority.LOW;
    }

    @Override
    public void validate(String name, Object object, CustomValidation annotation) throws InvalidConfigurationException {
        //noinspection ConstantConditions
        if (annotation == null || annotation.value() == null) {
            return;
        }
        Class<? extends ConfigurationValidator>[] validatorClasses = annotation.value();
        //noinspection ConstantConditions
        if (validatorClasses != null) {
            List<InvalidConfigurationException> errors = new ArrayList<>();
            for (Class<? extends ConfigurationValidator> validatorClass : validatorClasses) {
                try {
                    Constructor<? extends ConfigurationValidator> constructor = validatorClass.getDeclaredConstructor();
                    if (!constructor.isAccessible()) {
                        constructor.setAccessible(true);
                    }
                    ConfigurationValidator validator = constructor.newInstance();
                    try {
                        if (object.getClass().isArray()) {
                            Object[] objects = (Object[]) object;
                            for (Object o : objects) {
                                //noinspection unchecked
                                validator.validate(name, validator.getSupportedClass().cast(object));
                            }
                        } else {
                            //noinspection unchecked
                            validator.validate(name, validator.getSupportedClass().cast(object));
                        }
                    } catch (InvalidConfigurationException e) {
                        errors.add(e);
                    }
                } catch (NoSuchMethodException
                    | InstantiationException
                    | ClassCastException
                    | InvocationTargetException
                    | IllegalAccessException e) {
                    LOG.warn("Unable to use custom configuration validator. It's ignored", e);
                }
            }
            if (!errors.isEmpty()) {
                throw new InvalidConfigurationException(errors);
            }
        }
    }
}
