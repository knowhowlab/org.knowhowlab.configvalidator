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

package org.knowhowlab.configvalidator.service.internal;

import org.junit.Test;
import org.knowhowlab.configvalidator.api.annotations.CustomValidation;
import org.knowhowlab.configvalidator.api.annotations.FilterValidation;
import org.knowhowlab.configvalidator.api.annotations.NullValidation;
import org.knowhowlab.configvalidator.service.internal.helpers.MethodConfiguration1;
import org.knowhowlab.configvalidator.service.internal.helpers.SimpleConfiguration1;
import org.knowhowlab.configvalidator.service.internal.helpers.SimpleConfiguration2;
import org.knowhowlab.configvalidator.service.internal.validators.CustomValidator;
import org.knowhowlab.configvalidator.service.internal.validators.FilterValidator;
import org.knowhowlab.configvalidator.service.internal.validators.InternalConfigurationValidator;
import org.knowhowlab.configvalidator.service.internal.validators.NullValueValidator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.knowhowlab.configvalidator.service.internal.SimpleConfigurationValidationService.*;
import static org.knowhowlab.configvalidator.service.internal.validators.Priority.HIGHEST;

/**
 * @author dpishchukhin
 */
public class SimpleConfigurationValidationServiceTest {
    @Test
    public void testGetConfigurationName_simple1() throws Exception {
        assertThat(getConfigurationName(SimpleConfiguration1.class), is("SimpleConfiguration1"));
    }

    @Test
    public void testGetConfigurationName_simple2() throws Exception {
        assertThat(getConfigurationName(SimpleConfiguration2.class), is("test"));
    }

    @Test
    public void testGetPropertyName_method1() throws Exception {
        Method method = MethodConfiguration1.class.getDeclaredMethod("method1");
        assertThat(getPropertyName(method), is("method1"));
    }

    @Test
    public void testGetPropertyName_method2() throws Exception {
        Method method = MethodConfiguration1.class.getDeclaredMethod("method2");
        assertThat(getPropertyName(method), is("prop.1"));
    }

    @Test // todo
    public void testGetPropertyName_osgi_methods() throws Exception {
//        Method method = MethodConfiguration1.class.getDeclaredMethod("method2");
//        assertThat(getPropertyName(method), is("prop.1"));
    }

    @Test
    public void testFilter_noFilter() throws Exception {
        Map<Class, InternalConfigurationValidator> validators = new HashMap<>();
        validators.put(NullValueValidator.class, new NullValueValidator());
        validators.put(CustomValidator.class, new CustomValidator());

        Map<Class, InternalConfigurationValidator> map = filter(validators);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(2));
        assertThat(map.containsKey(NullValueValidator.class), is(true));
        assertThat(map.containsKey(CustomValidator.class), is(true));
    }

    @Test
    public void testFilter_filter() throws Exception {
        Map<Class, InternalConfigurationValidator> validators = new HashMap<>();
        validators.put(NullValueValidator.class, new NullValueValidator());
        validators.put(CustomValidator.class, new CustomValidator());

        Map<Class, InternalConfigurationValidator> map = filter(validators, HIGHEST);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(1));
        assertThat(map.containsKey(NullValueValidator.class), is(true));
    }

    @Test
    public void testSort() throws Exception {
        Map<Class, InternalConfigurationValidator> validators = new HashMap<>();
        validators.put(CustomValidation.class, new CustomValidator());
        validators.put(FilterValidation.class, new FilterValidator());
        validators.put(NullValidation.class, new NullValueValidator());

        LinkedHashMap<Class, InternalConfigurationValidator> map = sort(validators);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(3));
        assertThat(map.containsKey(NullValidation.class), is(true));
        assertThat(map.containsKey(FilterValidation.class), is(true));
        assertThat(map.containsKey(CustomValidation.class), is(true));

        List<Class> list = map.keySet().stream().collect(toList());
        assertThat(list.get(0), equalTo(NullValidation.class));
        assertThat(list.get(1), equalTo(FilterValidation.class));
        assertThat(list.get(2), equalTo(CustomValidation.class));
    }

    @Test
    public void testIsConfigProperty_method3() throws Exception {
        Method method = MethodConfiguration1.class.getDeclaredMethod("method3");
        assertThat(isConfigProperty(method), is(false));
    }

    @Test
    public void testIsConfigProperty_method4() throws Exception {
        Method method = MethodConfiguration1.class.getDeclaredMethod("method4");
        assertThat(isConfigProperty(method), is(true));
    }
}