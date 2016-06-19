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
import org.knowhowlab.configvalidator.api.annotations.StringEmptyValidation;

/**
 * @author dpishchukhin
 */
public class EmptyStringValidator implements InternalConfigurationValidator<String, StringEmptyValidation> {
    @Override
    public Priority getPriorityOrder() {
        return Priority.HIGH;
    }

    @Override
    public void validate(String name, String object, StringEmptyValidation annotation) throws InvalidConfigurationException {
        if (object.isEmpty()) {
            throw new InvalidConfigurationException(name, "is empty");
        }
    }
}
