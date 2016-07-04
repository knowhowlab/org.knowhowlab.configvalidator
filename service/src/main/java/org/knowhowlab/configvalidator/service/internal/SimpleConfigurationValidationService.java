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

package org.knowhowlab.configvalidator.service.internal;

import org.knowhowlab.configvalidator.api.InvalidConfigurationException;
import org.knowhowlab.configvalidator.api.annotations.*;
import org.knowhowlab.configvalidator.api.service.ConfigurationValidationService;
import org.knowhowlab.configvalidator.service.internal.validators.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.knowhowlab.configvalidator.service.internal.validators.Priority.*;

/**
 * @author dpishchukhin
 */
public class SimpleConfigurationValidationService implements ConfigurationValidationService {
    private static final Map<Class, InternalConfigurationValidator> CLASS_VALIDATORS = new HashMap<>();
    private static final Map<Class, InternalConfigurationValidator> METHOD_VALIDATORS = new HashMap<>();

    static {
        CLASS_VALIDATORS.put(NullValidation.class, new NullValueValidator());
        CLASS_VALIDATORS.put(CustomValidation.class, new CustomValidator());

        METHOD_VALIDATORS.put(NullValidation.class, new NullValueValidator());
        METHOD_VALIDATORS.put(ArrayValidation.class, new ArrayValidator());
        METHOD_VALIDATORS.put(CollectionValidation.class, new CollectionValidator());
        METHOD_VALIDATORS.put(CronValidation.class, new CronValidator());
        METHOD_VALIDATORS.put(FilterValidation.class, new FilterValidator());
        METHOD_VALIDATORS.put(NumbersRangeValidation.class, new NumbersRangeValidator());
        METHOD_VALIDATORS.put(RegexValidation.class, new StringRegexValidator());
        METHOD_VALIDATORS.put(EmptyStringValidation.class, new EmptyStringValidator());
        METHOD_VALIDATORS.put(ValuesValidation.class, new ValuesValidator());
        METHOD_VALIDATORS.put(CustomValidation.class, new CustomValidator());
    }

    @Override
    public <T> void validate(Class<T> aCLass, T config) throws InvalidConfigurationException {
        List<InvalidConfigurationException> errors = new ArrayList<>();
        // get class name
        String configName = getConfigurationName(aCLass);
        // validate HIGHEST for class
        errors.addAll(validate(configName, config, sort(filter(CLASS_VALIDATORS, HIGHEST))));
        // go method by method and run validation
        Method[] methods = aCLass.getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                if (isConfigProperty(method)) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    String propertyName = getPropertyName(method);
                    try {
                        errors.addAll(validate(propertyName, method.invoke(config), sort(filter(METHOD_VALIDATORS))));
                    } catch (Exception e) {
                        e.printStackTrace(); // todo
                    }
                }
            }
        }
        // validate <= HIGH for class
        errors.addAll(validate(configName, config, filter(CLASS_VALIDATORS, HIGH, MEDIUM, LOW, LOWEST)));

        if (!errors.isEmpty()) {
            throw new InvalidConfigurationException(errors);
        }
    }

    static Map<Class, InternalConfigurationValidator> filter(Map<Class, InternalConfigurationValidator> validators,
                                                             Priority... prios) {
        if (prios.length == 0) {
            return validators;
        } else {
            Set<Priority> set = new HashSet<>(asList(prios));
            return validators.entrySet().stream()
                    .filter(v -> set.contains(v.getValue().getPriorityOrder()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    static LinkedHashMap<Class, InternalConfigurationValidator> sort(Map<Class, InternalConfigurationValidator> validators) {
        return validators.entrySet().stream()
                .sorted(Comparator.comparing(v -> v.getValue().getPriorityOrder().getPriority()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        LinkedHashMap::new));
    }

    private <T> Collection<InvalidConfigurationException> validate(String name,
                                                                   T value,
                                                                   Map<Class, InternalConfigurationValidator> validators) {
        List<InvalidConfigurationException> errors = new ArrayList<>();
        // todo
        return errors;
    }

    static String getPropertyName(Method method) {
        ConfigurationProperty annotation = method.getAnnotation(ConfigurationProperty.class);
        if (annotation == null) {
            return method.getName(); // todo: osgi names
        } else {
            return annotation.value();
        }
    }

    static boolean isConfigProperty(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(v -> METHOD_VALIDATORS.keySet().contains(v));
    }

    static <T> String getConfigurationName(Class<T> aCLass) {
        ConfigurationClass annotation = aCLass.getAnnotation(ConfigurationClass.class);
        if (annotation == null) {
            return aCLass.getSimpleName();
        } else {
            return annotation.value();
        }
    }
}
