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

package org.knowhowlab.configvalidator.service.internal.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.knowhowlab.configvalidator.service.internal.utils.DoubleRange.valueOf;

/**
 * @author dpishchukhin
 */
public class DoubleRangeTest {
    @Test
    public void valueOf_simple() throws Exception {
        DoubleRange range = valueOf("[1,2]");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(1.));
        assertThat(range.getMaximum(), is(2.));
        assertThat(range.isMinimumIncluded(), is(true));
        assertThat(range.isMaximumIncluded(), is(true));

        range = valueOf("(1,2]");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(1.));
        assertThat(range.getMaximum(), is(2.));
        assertThat(range.isMinimumIncluded(), is(false));
        assertThat(range.isMaximumIncluded(), is(true));

        range = valueOf("(1,2)");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(1.));assertThat(range.getMaximum(), is(2.));
        assertThat(range.isMinimumIncluded(), is(false));
        assertThat(range.isMaximumIncluded(), is(false));

        range = valueOf("[1,2)");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(1.));
        assertThat(range.getMaximum(), is(2.));
        assertThat(range.isMinimumIncluded(), is(true));
        assertThat(range.isMaximumIncluded(), is(false));

        range = valueOf("(,2)");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(Double.NEGATIVE_INFINITY));
        assertThat(range.getMaximum(), is(2.));
        assertThat(range.isMinimumIncluded(), is(false));
        assertThat(range.isMaximumIncluded(), is(false));

        range = valueOf("(1,)");
        assertThat(range, notNullValue());
        assertThat(range.getMinimum(), is(1.));
        assertThat(range.getMaximum(), is(Double.POSITIVE_INFINITY));
        assertThat(range.isMinimumIncluded(), is(false));
        assertThat(range.isMaximumIncluded(), is(false));
    }

}