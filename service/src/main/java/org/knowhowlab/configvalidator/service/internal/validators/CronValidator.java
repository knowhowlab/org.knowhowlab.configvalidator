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
import org.knowhowlab.configvalidator.api.annotations.CronValidation;
import org.knowhowlab.configvalidator.service.internal.utils.CronExpression;

import java.util.Arrays;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class CronValidator implements InternalConfigurationValidator<String, CronValidation> {
    private static final List<String> RESERVED_FORMATS = Arrays.asList(
        "@reboot",
        "@yearly",
        "@annually",
        "@monthly",
        "@weekly",
        "@daily",
        "@midnight",
        "@hourly"
    );

    @Override
    public Priority getPriorityOrder() {
        return Priority.MEDIUM;
    }

    @Override
    public void validate(String name, String object, CronValidation annotation) throws InvalidConfigurationException {
        if (!RESERVED_FORMATS.contains(object) && !CronExpression.isValid(object)) {
            throw new InvalidConfigurationException(name, "is invalid cron expression");
        }
    }
}
