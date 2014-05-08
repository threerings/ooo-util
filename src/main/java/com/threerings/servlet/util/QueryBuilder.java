//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import com.samskivert.util.StringUtil;
import com.samskivert.util.Tuple;

public class QueryBuilder
{
    /**
     * Build a query directly from the specified map.
     */
    public static String build (Map<?, ?> values)
    {
        return encode(new StringBuilder(), values.entrySet());
    }

    /**
     * Retrieve the only value for the given key, or null if it's not defined. If the key has more
     * than one value defined, an IllegalArgumentException will be thrown.
     */
    public String getOnly (String key)
    {
        return getOnly(key, null);
    }

    /**
     * Retrieve the only value for the given key, or the default value if it's not defined. If the
     * key has more than one value defined, an IllegalArgumentException will be thrown.
     */
    public String getOnly (String key, String defaultValue)
    {
        return Iterables.getOnlyElement(get(key), defaultValue);
    }

    /**
     * Retrieve the first value for the given key, or null if the key is not defined.
     */
    public String getFirst (String key)
    {
        Collection<String> values = get(key);
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * Returns the current values for key or an empty collection if there isn't a value.
     */
    public Collection<String> get (String key)
    {
        return _params.get(key);
    }

    /**
     * Adds the given value to the parameter mapping under key adding to any current value.
     * {@link String#valueOf} is called on <code>val</code> to turn it into a string.
     */
    public QueryBuilder add (String key, Object val)
    {
        _params.put(key, String.valueOf(val));
        return this;
    }

    /**
     * Adds all the parameters in <code>req</code> to the parameters being constructed.
     */
    public QueryBuilder addAll (HttpServletRequest req)
    {
        for (Tuple<String, String> entry : new Parameters(req).entries()) {
            add(entry.left, entry.right);
        }
        return this;
    }

    /**
     * Adds all the parameters in <code>map</code> to the parameters being constructed.
     */
    public QueryBuilder addAll (Map<?, ?> map)
    {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            add(String.valueOf(entry.getKey()), entry.getValue());
        }
        return this;
    }

    /**
     * Appends the query in this builder to the given StringBuilder with a question mark.
     */
    public String toUrl (StringBuilder base)
    {
        return _params.isEmpty() ? base.toString() : encode(base.append('?'), _params.entries());
    }

    /**
     * Appends the query in this builder to the given String with a question mark.
     */
    public String toUrl (String base)
    {
        return toUrl(new StringBuilder(base));
    }

    /**
     * Returns the query in this builder.
     */
    public String toString ()
    {
        return encode(new StringBuilder(), _params.entries());
    }

    protected static String encode (StringBuilder out, Iterable<? extends Map.Entry<?, ?>> entries)
    {
        boolean appended = false;
        for (Map.Entry<?, ?> entry : entries) {
            out.append(StringUtil.encode(String.valueOf(entry.getKey()))).
                append('=').
                append(StringUtil.encode(String.valueOf(entry.getValue()))).
                append('&');
            appended = true;
        }
        if (appended) { // Drop the extra ampersand
            out.setLength(out.length() - 1);
        }
        return out.toString();
    }

    protected final Multimap<String, String> _params = ArrayListMultimap.create();
}
