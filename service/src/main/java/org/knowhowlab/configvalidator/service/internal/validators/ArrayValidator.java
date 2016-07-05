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
import org.knowhowlab.configvalidator.api.annotations.ArrayValidation;
import org.knowhowlab.configvalidator.api.annotations.Range;
import org.knowhowlab.configvalidator.service.internal.utils.DoubleRange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author dpishchukhin
 */
public class ArrayValidator implements InternalConfigurationValidator<Object[], ArrayValidation> {
    @Override
    public Priority getPriorityOrder() {
        return Priority.MEDIUM;
    }

    @Override
    public void validate(String name, Object[] array, ArrayValidation annotation) throws InvalidConfigurationException {
        //noinspection ConstantConditions
        if (annotation != null && annotation.length() != null) {
            List<DoubleRange> ranges = Arrays.stream(annotation.length())
                .map(Range::value)
                .filter(v -> v != null)
                .map(DoubleRange::valueOf)
                .filter(DoubleRange::isValid)
                .collect(Collectors.toList());
            if (ranges.isEmpty()) {
                return;
            }
            if (!ranges.stream().anyMatch(v -> v.contains(array.length))) {
                throw new InvalidConfigurationException(name,
                    format("array length '%s' out of range %s", array.length,
                        Arrays.toString(ranges.toArray(new DoubleRange[ranges.size()]))));
            }
        }
    }
}
