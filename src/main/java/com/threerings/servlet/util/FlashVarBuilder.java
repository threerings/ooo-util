//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import com.samskivert.util.Tuple;

import static com.threerings.servlet.util.Log.log;

public class FlashVarBuilder
{
    /**
     * Adds the given value to the parameter mapping under key replacing any current value.
     * {@link String#valueOf} is called on <code>val</code> to turn it into a string.
     */
    public FlashVarBuilder add (String key, Object val)
    {
        _params.put(key, String.valueOf(val));
        return this;
    }

    /**
     * Adds all the parameters in <code>req</code> to the parameters being constructed. If there's
     * already a mapping for a given parameter name, the original value is left in place and a
     * warning is printed.
     */
    public FlashVarBuilder addAll (HttpServletRequest req)
    {
        for (Tuple<String, String> entry : new Parameters(req).entries()) {
            if (_params.containsKey(entry.left)) {
                log.warning("Request contained an already defined value", "name", entry.left);
            } else {
                _params.put(entry.left, entry.right);
            }
        }
        return this;
    }

    @Override
    public String toString ()
    {
        return QueryBuilder.encode(new StringBuilder(), _params.entrySet());
    }

    protected final Map<String, String> _params = Maps.newHashMap();
}
