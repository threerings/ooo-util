//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import com.google.common.base.Function;

public class Converters
{
    public static final Function<String, Long> TO_LONG = new Function<String, Long>() {
        public Long apply (String from) {
            return Long.parseLong(from);
        }
    };

    public static final Function<String, Boolean> TO_BOOLEAN = new Function<String, Boolean>() {
        public Boolean apply (String from) {
            return Boolean.parseBoolean(from);
        }
    };

    public static final Function<String, Integer> TO_INT = new Function<String, Integer>() {
        public Integer apply (String from) {
            return Integer.parseInt(from);
        }
    };

    public static final Function<String, Byte> TO_BYTE = new Function<String, Byte>() {
        public Byte apply (String from) {
            return Byte.parseByte(from);
        }
    };

    public static final Function<String, Float> TO_FLOAT = new Function<String, Float>() {
        public Float apply (String from) {
            return Float.parseFloat(from);
        }
    };
}
