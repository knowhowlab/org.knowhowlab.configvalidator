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
import org.knowhowlab.configvalidator.api.annotations.CustomValidation;
import org.knowhowlab.configvalidator.service.internal.helpers.ErrorStringValidator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author dpishchukhin
 */
public class CustomValidatorTest {
    private CustomValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new CustomValidator();
    }

    @Test
    public void value_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", "123", null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_values() throws Exception {
        try {
            validator.validate("param.1", "123", mock(CustomValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_valid() throws Exception {
        try {
            CustomValidation mock = mock(CustomValidation.class);
            when(mock.value()).thenReturn(new Class[]{ErrorStringValidator.class});
            validator.validate("param.1", "123", mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_invalid() throws Exception {
        CustomValidation mock = mock(CustomValidation.class);
        when(mock.value()).thenReturn(new Class[]{ErrorStringValidator.class});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", "errorString", mock))
                .withMessage("param.1 starts with error");
    }
}