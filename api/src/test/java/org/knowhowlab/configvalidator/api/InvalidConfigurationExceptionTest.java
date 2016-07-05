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

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author dpishchukhin
 */
public class InvalidConfigurationExceptionTest {
    @Test
    public void one_property() throws Exception {
        Assert.assertThat(new InvalidConfigurationException("param.1", "is invalid").getMessage(),
            equalTo("param.1 is invalid"));
    }

    @Test
    public void property_name_null() throws Exception {
        Assert.assertThat(new InvalidConfigurationException(null, "is invalid").getMessage(),
            equalTo("unknown is invalid"));
    }
}
