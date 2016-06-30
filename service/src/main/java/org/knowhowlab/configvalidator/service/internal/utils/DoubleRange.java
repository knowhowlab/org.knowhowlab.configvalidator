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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dpishchukhin
 */
public class DoubleRange {
    private boolean valid = false;

    private double minimum = Double.NEGATIVE_INFINITY;

    private double maximum = Double.POSITIVE_INFINITY;

    private boolean minimumIncluded = false;

    private boolean maximumIncluded = false;

    private static final Pattern pattern = Pattern.compile("^(\\(|\\[)(.*),(.*)(\\)|\\])$");


    private DoubleRange(double minimum, boolean minimumIncluded, double maximum, boolean maximumIncluded) {
        this.minimum = minimum;
        this.minimumIncluded = minimumIncluded;
        this.maximum = maximum;
        this.maximumIncluded = maximumIncluded;
        this.valid = true;
    }

    public DoubleRange() {
    }

    public static DoubleRange valueOf(String value) throws IllegalArgumentException {
        return parse(value);
    }

    public boolean isValid() {
        return valid && maximum >= minimum;
    }

    private static DoubleRange parse(String value) {
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            return new DoubleRange();
        }
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        boolean minIncluded = false;
        boolean maxIncluded = false;

        if ("[".equals(matcher.group(1))) {
            minIncluded = true;
        }
        if ("]".equals(matcher.group(4))) {
            maxIncluded = true;
        }
        if (!matcher.group(2).isEmpty()) {
            try {
                min = Double.parseDouble(matcher.group(2));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Range (minimum) is invalid: %s", value));
            }
        }
        if (!matcher.group(3).isEmpty()) {
            try {
                max = Double.parseDouble(matcher.group(3));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Range (maximum) is invalid: %s", value));
            }
        }

        return new DoubleRange(min, minIncluded, max, maxIncluded);
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public boolean isMinimumIncluded() {
        return minimumIncluded;
    }

    public boolean isMaximumIncluded() {
        return maximumIncluded;
    }

    public boolean contains(Number value) {
        return ((minimumIncluded && minimum == value.doubleValue()) || minimum < value.doubleValue()) //
            && ((maximumIncluded && maximum == value.doubleValue()) || maximum > value.doubleValue());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(minimumIncluded ? "[" : "(");
        builder.append(Double.isInfinite(minimum) ? "" : minimum);
        builder.append(",");
        builder.append(Double.isInfinite(maximum) ? "" : maximum);
        builder.append(maximumIncluded ? "]" : ")");
        return builder.toString();
    }
}
