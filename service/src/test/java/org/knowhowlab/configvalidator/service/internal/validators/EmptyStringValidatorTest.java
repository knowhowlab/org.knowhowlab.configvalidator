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

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowhowlab.configvalidator.api.InvalidConfigurationException;
import org.knowhowlab.configvalidator.api.annotations.EmptyStringValidation;

import static org.mockito.Mockito.mock;

/**
 * @author dpishchukhin
 */
public class EmptyStringValidatorTest {
    private EmptyStringValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new EmptyStringValidator();
    }

    @Test
    public void not_empty_value() throws Exception {
        try {
            validator.validate("param.1", "value", mock(EmptyStringValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void empty_value() throws Exception {
        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
            .isThrownBy(() -> validator.validate("param.1", "", mock(EmptyStringValidation.class)))
            .withMessage("param.1 is empty");
    }

    @Test
    public void empty_value_and_null_annotation() throws Exception {
        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
            .isThrownBy(() -> validator.validate("param.1", "", null))
            .withMessage("param.1 is empty");
    }
}
