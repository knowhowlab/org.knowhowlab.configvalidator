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

package org.knowhowlab.configvalidator.service.internal.helpers;

import org.knowhowlab.configvalidator.api.ConfigurationValidator;
import org.knowhowlab.configvalidator.api.InvalidConfigurationException;

/**
 * @author dpishchukhin
 */
public class ErrorStringValidator implements ConfigurationValidator<String> {
    private ErrorStringValidator() {
    }

    @Override
    public Class<String> getSupportedClass() {
        return String.class;
    }

    @Override
    public void validate(String name, String object) throws InvalidConfigurationException {
        if (object.startsWith("error")) {
            throw new InvalidConfigurationException(name, "starts with error");
        }
    }
}
