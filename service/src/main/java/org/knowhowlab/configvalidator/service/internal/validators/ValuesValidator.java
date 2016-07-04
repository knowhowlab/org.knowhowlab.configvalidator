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

import org.knowhowlab.configvalidator.api.InvalidConfigurationException;
import org.knowhowlab.configvalidator.api.annotations.Value;
import org.knowhowlab.configvalidator.api.annotations.ValuesValidation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * @author dpishchukhin
 */
public class ValuesValidator implements InternalConfigurationValidator<Object, ValuesValidation> {
    @Override
    public Priority getPriorityOrder() {
        return Priority.MEDIUM;
    }

    @Override
    public void validate(String name, Object object, ValuesValidation annotation) throws InvalidConfigurationException {
        //noinspection ConstantConditions
        if (annotation != null && annotation.value() != null) {
            List<String> values = Arrays.stream(annotation.value())
                    .map(Value::value)
                    .filter(v -> v != null)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            if (values.isEmpty()) {
                return;
            }
            if (object.getClass().isArray()) {
                List<String> invalidValues = Arrays.stream((Object[]) object)
                        .map(String::valueOf)
                        .filter(v -> !values.contains(v))
                        .collect(Collectors.toList());
                if (!invalidValues.isEmpty()) {
                    throw new InvalidConfigurationException(name,
                            format("contains values %s out of range %s",
                                    Arrays.toString(invalidValues.toArray(new String[invalidValues.size()])),
                                    Arrays.toString(values.toArray(new String[values.size()]))));
                }
            } else {
                if (!values.stream().anyMatch(v -> v.equals(valueOf(object)))) {
                    throw new InvalidConfigurationException(name,
                            format("contains value '%s' out of range %s", object,
                                    Arrays.toString(values.toArray(new String[values.size()]))));
                }
            }
        }
    }
}
