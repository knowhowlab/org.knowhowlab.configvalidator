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
import org.knowhowlab.configvalidator.api.annotations.NumbersRangeValidation;
import org.knowhowlab.configvalidator.api.annotations.Range;
import org.knowhowlab.configvalidator.service.internal.utils.DoubleRange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author dpishchukhin
 */
public class NumbersRangeValidator implements InternalConfigurationValidator<Object, NumbersRangeValidation> {
    @Override
    public Priority getPriorityOrder() {
        return Priority.MEDIUM;
    }

    @Override
    public void validate(String name, Object object, NumbersRangeValidation annotation) throws InvalidConfigurationException {
        //noinspection ConstantConditions
        if (annotation != null && annotation.value() != null) {
            List<DoubleRange> ranges = Arrays.stream(annotation.value())
                    .map(Range::value)
                    .filter(v -> v != null)
                    .map(DoubleRange::valueOf)
                    .filter(DoubleRange::isValid)
                    .collect(Collectors.toList());
            if (ranges.isEmpty()) {
                return;
            }
            if (object.getClass().isArray()) {
                List<Object> invalidValues = Arrays.stream((Object[]) object)
                        .filter(v -> !(v instanceof Number))
                        .collect(Collectors.toList());
                List<Object> outOfRangeValues = Arrays.stream((Object[]) object)
                        .filter(v -> v instanceof Number)
                        .map(v -> (Number)v)
                        .filter(v -> ranges.stream().noneMatch(s -> s.contains(v)))
                        .collect(Collectors.toList());

                if (!invalidValues.isEmpty() || !outOfRangeValues.isEmpty()) {
                    invalidValues.addAll(outOfRangeValues);
                    throw new InvalidConfigurationException(name,
                            format("contains %s invalid or out of range %s values",
                                    Arrays.toString(invalidValues.toArray(new Object[invalidValues.size()])),
                                    Arrays.toString(ranges.toArray(new DoubleRange[ranges.size()]))));
                }
            } else {
                if (!(object instanceof Number)) {
                    throw new InvalidConfigurationException(name,
                            format("is not a number '%s'", object));
                }
                if (!ranges.stream().anyMatch(v -> v.contains((Number)object))) {
                    throw new InvalidConfigurationException(name,
                            format("contains value '%s' out of range %s", object,
                                    Arrays.toString(ranges.toArray(new DoubleRange[ranges.size()]))));
                }
            }
        }
    }
}
