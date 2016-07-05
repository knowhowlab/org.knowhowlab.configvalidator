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
import org.knowhowlab.configvalidator.api.annotations.CollectionValidation;
import org.knowhowlab.configvalidator.api.annotations.Range;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author dpishchukhin
 */
public class CollectionValidatorTest {
    private CollectionValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new CollectionValidator();
    }

    @Test
    public void value_empty_annotation() throws Exception {
        try {
            validator.validate("param.1", emptyList(), null);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_values() throws Exception {
        try {
            validator.validate("param.1", emptyList(), mock(CollectionValidation.class));
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_empty_value() throws Exception {
        try {
            CollectionValidation mock = mock(CollectionValidation.class);
            Range rangeMock = mock(Range.class);
            when(mock.size()).thenReturn(new Range[]{rangeMock});
            validator.validate("param.1", emptyList(), mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_invalid_value() throws Exception {
        try {
            CollectionValidation mock = mock(CollectionValidation.class);
            Range rangeMock = mock(Range.class);
            when(rangeMock.value()).thenReturn("[0,");
            when(mock.size()).thenReturn(new Range[]{rangeMock});
            validator.validate("param.1", emptyList(), mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_contains() throws Exception {
        try {
            CollectionValidation mock = mock(CollectionValidation.class);
            Range rangeMock1 = mock(Range.class);
            when(rangeMock1.value()).thenReturn("[0,0]");
            Range valueMock2 = mock(Range.class);
            when(valueMock2.value()).thenReturn("[2,3]");
            when(mock.size()).thenReturn(new Range[]{rangeMock1, valueMock2});
            validator.validate("param.1", emptyList(), mock);
        } catch (InvalidConfigurationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void value_does_not_contain() throws Exception {
        CollectionValidation mock = mock(CollectionValidation.class);
        Range rangeMock1 = mock(Range.class);
        when(rangeMock1.value()).thenReturn("[1,2)");
        Range valueMock2 = mock(Range.class);
        when(valueMock2.value()).thenReturn("[2,3]");
        when(mock.size()).thenReturn(new Range[]{rangeMock1, valueMock2});

        Assertions.assertThatExceptionOfType(InvalidConfigurationException.class)
            .isThrownBy(() -> validator.validate("param.1", emptyList(), mock))
            .withMessage("param.1 collection size '0' out of range [[1.0,2.0), [2.0,3.0]]");
    }
}
