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
import org.knowhowlab.configvalidator.api.annotations.Value;
import org.knowhowlab.configvalidator.api.annotations.ValuesValidation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author dpishchukhin
 */
public class ValuesValidatorTest {
    private ValuesValidator validator;

    private enum TestEnum {
        OK, NOK, NOKNOK
    }

    @Before
    public void setUp() throws Exception {
        validator = new ValuesValidator();
    }

    @Test
    public void value_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", "value", null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_enum_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", TestEnum.OK, null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", new String[]{"value"}, null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_enum_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", new TestEnum[]{TestEnum.OK, TestEnum.NOK}, null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_values() throws Exception {
        try {
            validator.validate("param.1", "value", mock(ValuesValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_enum_empty_values() throws Exception {
        try {
            validator.validate("param.1", TestEnum.OK, mock(ValuesValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_values() throws Exception {
        try {
            validator.validate("param.1", new String[]{"value"}, mock(ValuesValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_enum_empty_values() throws Exception {
        try {
            validator.validate("param.1", new TestEnum[]{TestEnum.OK, TestEnum.NOK}, mock(ValuesValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_value() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock = mock(Value.class);
            when(mock.value()).thenReturn(new Value[]{valueMock});
            validator.validate("param.1", "value", mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_enum_empty_value() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock = mock(Value.class);
            when(mock.value()).thenReturn(new Value[]{valueMock});
            validator.validate("param.1", TestEnum.OK, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_value() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock = mock(Value.class);
            when(mock.value()).thenReturn(new Value[]{valueMock});
            validator.validate("param.1", new String[]{"value"}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_enum_empty_value() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock = mock(Value.class);
            when(mock.value()).thenReturn(new Value[]{valueMock});
            validator.validate("param.1", new TestEnum[]{TestEnum.OK, TestEnum.NOK}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_contains() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock1 = mock(Value.class);
            when(valueMock1.value()).thenReturn("a");
            Value valueMock2 = mock(Value.class);
            when(valueMock2.value()).thenReturn("b");
            when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});
            validator.validate("param.1", "a", mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_enum_contains() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock1 = mock(Value.class);
            when(valueMock1.value()).thenReturn("OK");
            Value valueMock2 = mock(Value.class);
            when(valueMock2.value()).thenReturn("NOK");
            when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});
            validator.validate("param.1", TestEnum.OK, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_contain() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock1 = mock(Value.class);
            when(valueMock1.value()).thenReturn("a");
            Value valueMock2 = mock(Value.class);
            when(valueMock2.value()).thenReturn("b");
            Value valueMock3 = mock(Value.class);
            when(valueMock3.value()).thenReturn("c");
            when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2, valueMock3});
            validator.validate("param.1", new String[]{"a", "b"}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_enum_contain() throws Exception {
        try {
            ValuesValidation mock = mock(ValuesValidation.class);
            Value valueMock1 = mock(Value.class);
            when(valueMock1.value()).thenReturn("OK");
            Value valueMock2 = mock(Value.class);
            when(valueMock2.value()).thenReturn("NOK");
            Value valueMock3 = mock(Value.class);
            when(valueMock3.value()).thenReturn("NOKNOK");
            when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2, valueMock3});
            validator.validate("param.1", new TestEnum[]{TestEnum.OK, TestEnum.NOK}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_does_not_contain() throws Exception {
        ValuesValidation mock = mock(ValuesValidation.class);
        Value valueMock1 = mock(Value.class);
        when(valueMock1.value()).thenReturn("a");
        Value valueMock2 = mock(Value.class);
        when(valueMock2.value()).thenReturn("b");
        when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", "c", mock))
                .withMessage("param.1 contains value 'c' out of range [a, b]");
    }

    @Test
    public void value_enum_does_not_contain() throws Exception {
        ValuesValidation mock = mock(ValuesValidation.class);
        Value valueMock1 = mock(Value.class);
        when(valueMock1.value()).thenReturn("OK");
        Value valueMock2 = mock(Value.class);
        when(valueMock2.value()).thenReturn("NOK");
        when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", TestEnum.NOKNOK, mock))
                .withMessage("param.1 contains value 'NOKNOK' out of range [OK, NOK]");
    }

    @Test
    public void values_do_not_contain() throws Exception {
        ValuesValidation mock = mock(ValuesValidation.class);
        Value valueMock1 = mock(Value.class);
        when(valueMock1.value()).thenReturn("a");
        Value valueMock2 = mock(Value.class);
        when(valueMock2.value()).thenReturn("b");
        when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", new String[]{"a", "c"}, mock))
                .withMessage("param.1 contains values [c] out of range [a, b]");
    }

    @Test
    public void values_enum_do_not_contain() throws Exception {
        ValuesValidation mock = mock(ValuesValidation.class);
        Value valueMock1 = mock(Value.class);
        when(valueMock1.value()).thenReturn("OK");
        Value valueMock2 = mock(Value.class);
        when(valueMock2.value()).thenReturn("NOK");
        when(mock.value()).thenReturn(new Value[]{valueMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", new TestEnum[]{TestEnum.OK, TestEnum.NOKNOK}, mock))
                .withMessage("param.1 contains values [NOKNOK] out of range [OK, NOK]");
    }
}