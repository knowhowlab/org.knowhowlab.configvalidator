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
import org.knowhowlab.configvalidator.api.annotations.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author dpishchukhin
 */
public class NumbersRangeValidatorTest {
    private NumbersRangeValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new NumbersRangeValidator();
    }

    @Test
    public void value_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", 1, null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", new Number[]{1, 2}, null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_values() throws Exception {
        try {
            validator.validate("param.1", 1, mock(NumbersRangeValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_values() throws Exception {
        try {
            validator.validate("param.1", new Number[]{1, 2}, mock(NumbersRangeValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_value() throws Exception {
        try {
            NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
            Range rangeMock = mock(Range.class);
            when(mock.value()).thenReturn(new Range[]{rangeMock});
            validator.validate("param.1", 1, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_empty_value() throws Exception {
        try {
            NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
            Range rangeMock = mock(Range.class);
            when(mock.value()).thenReturn(new Range[]{rangeMock});
            validator.validate("param.1", new Number[]{1, 2}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_contains() throws Exception {
        try {
            NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
            Range rangeMock1 = mock(Range.class);
            when(rangeMock1.value()).thenReturn("[0,0]");
            Range valueMock2 = mock(Range.class);
            when(valueMock2.value()).thenReturn("[2,3]");
            when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});
            validator.validate("param.1", 2, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void values_contain() throws Exception {
        try {
            NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
            Range rangeMock1 = mock(Range.class);
            when(rangeMock1.value()).thenReturn("[0,0]");
            Range valueMock2 = mock(Range.class);
            when(valueMock2.value()).thenReturn("[2,3]");
            when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});
            validator.validate("param.1", new Number[]{0, 2}, mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_does_not_contain() throws Exception {
        NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
        Range rangeMock1 = mock(Range.class);
        when(rangeMock1.value()).thenReturn("[0,0]");
        Range valueMock2 = mock(Range.class);
        when(valueMock2.value()).thenReturn("[2,3]");
        when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", 1, mock))
                .withMessage("param.1 contains value '1' out of range [[0.0,0.0], [2.0,3.0]]");
    }

    @Test
    public void value_invalid() throws Exception {
        NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
        Range rangeMock1 = mock(Range.class);
        when(rangeMock1.value()).thenReturn("[0,0]");
        Range valueMock2 = mock(Range.class);
        when(valueMock2.value()).thenReturn("[2,3]");
        when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", "1", mock))
                .withMessage("param.1 is not a number '1'");
    }

    @Test
    public void values_do_not_contain() throws Exception {
        NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
        Range rangeMock1 = mock(Range.class);
        when(rangeMock1.value()).thenReturn("[0,0]");
        Range valueMock2 = mock(Range.class);
        when(valueMock2.value()).thenReturn("[2,3]");
        when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", new Number[]{0.5, 1, 2}, mock))
                .withMessage("param.1 contains [0.5, 1] invalid or out of range [[0.0,0.0], [2.0,3.0]] values");
    }

    @Test
    public void values_invalid() throws Exception {
        NumbersRangeValidation mock = mock(NumbersRangeValidation.class);
        Range rangeMock1 = mock(Range.class);
        when(rangeMock1.value()).thenReturn("[0,0]");
        Range valueMock2 = mock(Range.class);
        when(valueMock2.value()).thenReturn("[2,3]");
        when(mock.value()).thenReturn(new Range[]{rangeMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
                .isThrownBy(() -> validator.validate("param.1", new Object[]{"a", 2}, mock))
                .withMessage("param.1 contains [a] invalid or out of range [[0.0,0.0], [2.0,3.0]] values");
    }
}