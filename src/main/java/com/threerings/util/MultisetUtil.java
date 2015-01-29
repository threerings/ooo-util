//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.util;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

/**
 * Convenience functions for working with Multisets.
 */
public class MultisetUtil
{
    /** An Ordering that compares Multiset.Entrys by count. */
    public static final Ordering<Multiset.Entry<?>> ENTRY_COUNT_ORDERING =
        new Ordering<Multiset.Entry<?>>() {
            public int compare (Multiset.Entry<?> left, Multiset.Entry<?> right) {
                return Ints.compare(left.getCount(), right.getCount());
            }
        };

    /**
     * Return a Function that will transform objects to their corresponding count in the
     * specified Multiset.
     */
    public static Function<Object, Integer> countFunction (final Multiset<?> set)
    {
        return new Function<Object, Integer>() {
            public Integer apply (Object o) {
                return set.count(o);
            }
        };
    }

    /**
     * Create an ImmutableMultiset with a single element with the specified count.
     */
    public static <T> ImmutableMultiset<T> singleton (T element, int count)
    {
        return ImmutableMultiset.<T>builder()
            .addCopies(element, count)
            .build();
    }
}
