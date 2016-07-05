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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.knowhowlab.configvalidator.service.internal.SimpleConfigurationValidationService.*;
import static org.knowhowlab.configvalidator.service.internal.validators.Priority.HIGHEST;
import static org.mockito.Mockito.mock;

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
        validators.put(NullValidation.class, new NullValueValidator());
        validators.put(CustomValidation.class, new CustomValidator());

        Map<Annotation, InternalConfigurationValidator> map = filter(validators, new Annotation[0]);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(0));
    }

    @Test
    public void testFilter_filter_annotations() throws Exception {
        Map<Class, InternalConfigurationValidator> validators = new HashMap<>();
        NullValueValidator nullValueValidator = new NullValueValidator();
        validators.put(NullValidation.class, nullValueValidator);
        validators.put(CustomValidation.class, new CustomValidator());

        Map<Annotation, InternalConfigurationValidator> map = filter(validators,
            MethodConfiguration1.class.getDeclaredMethod("method4").getDeclaredAnnotations());
        assertThat(map, notNullValue());
        assertThat(map.size(), is(1));
        assertThat(map.containsValue(nullValueValidator), is(true));
    }

    @Test
    public void testFilter_filter_prio() throws Exception {
        Map<Class, InternalConfigurationValidator> validators = new HashMap<>();
        NullValueValidator nullValueValidator = new NullValueValidator();
        validators.put(NullValidation.class, nullValueValidator);
        validators.put(CustomValidation.class, new CustomValidator());

        Map<Annotation, InternalConfigurationValidator> map = filter(validators,
            MethodConfiguration1.class.getDeclaredMethod("method5").getDeclaredAnnotations(), HIGHEST);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(1));
        assertThat(map.containsValue(nullValueValidator), is(true));
    }

    @Test
    public void testSort() throws Exception {
        NullValidation nullValidation = mock(NullValidation.class);
        CustomValidation customValidation = mock(CustomValidation.class);
        FilterValidation filterValidation = mock(FilterValidation.class);

        Map<Annotation, InternalConfigurationValidator> validators = new HashMap<>();
        validators.put(customValidation, new CustomValidator());
        validators.put(filterValidation, new FilterValidator());
        validators.put(nullValidation, new NullValueValidator());

        LinkedHashMap<Annotation, InternalConfigurationValidator> map = sort(validators);
        assertThat(map, notNullValue());
        assertThat(map.size(), is(3));
        assertThat(map.containsKey(nullValidation), is(true));
        assertThat(map.containsKey(filterValidation), is(true));
        assertThat(map.containsKey(customValidation), is(true));

        List<Annotation> list = map.keySet().stream().collect(toList());
        assertThat(list.get(0), equalTo(nullValidation));
        assertThat(list.get(1), equalTo(filterValidation));
        assertThat(list.get(2), equalTo(customValidation));
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
