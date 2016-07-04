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

package org.knowhowlab.configvalidator.api;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

/**
 * @author dpishchukhin
 */
public class InvalidConfigurationException extends IllegalArgumentException {
    private static final String UNKNOWN_NAME = "unknown";

    public InvalidConfigurationException(String name, String validationError) {
        super(createMessage(name, validationError));
    }

    public InvalidConfigurationException(Collection<InvalidConfigurationException> errors) {
        super(errors.stream()
                .map(Exception::getMessage)
                .collect(joining(", "))
        );
    }

    private static String createMessage(String name, String validationError) {
        return (name == null ? UNKNOWN_NAME : name) + " " + validationError;
    }
}
