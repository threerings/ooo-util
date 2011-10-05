//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletRequest;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.samskivert.util.Logger;
import com.samskivert.util.Tuple;

/**
 * Provides convenient access to and conversion of the parameters of an HttpServletRequest.
 */
public class Parameters
{
    public Parameters (ServletRequest req)
    {
        _req = req;
    }

    /**
     * Returns an Iterable over all the parameter names for this request.
     */
    public Set<String> names ()
    {
        @SuppressWarnings("unchecked")
        Set<String> names = _req.getParameterMap().keySet();
        return names;
    }

    /**
     * Returns a Collection of all the parameter values for this request.
     */
    public Collection<String[]> values ()
    {
        @SuppressWarnings("unchecked")
        Collection<String[]> vals = _req.getParameterMap().values();
        return vals;
    }

    /**
     * Returns all the names and values in the parameters. The name is {@link Tuple#left} and the
     * value is {@link Tuple#right}. If a name appears multiple times, it'll be in multiple
     * tuples.
     */
    public Collection<Tuple<String, String>> entries ()
    {
        Set<String> names = names();
        List<Tuple<String, String>> entries = Lists.newArrayListWithCapacity(names.size());
        for (String name : names) {
            for (String val : getAll(name)) {
                entries.add(Tuple.newTuple(name, val));
            }
        }
        return entries;
    }

    /**
     * Returns true if the request contains the given parameter.
     */
    public boolean has (String name)
    {
        return _req.getParameterValues(name) != null;
    }

    /**
     * Returns the value for the parameter on this request or null if it has no such parameter.
     * IllegalStateException is thrown if the request has multiple values for the parameter.
     */
    public String get (String name)
    {
        return get(name, (String)null);
    }

    /**
     * Returns the value for the parameter on this request or <code>def</code> if it has no such
     * parameter. IllegalStateException is thrown if the request has multiple values for the
     * parameter.
     */
    public String get (String name, String def)
    {
        String[] vals = _req.getParameterValues(name);
        if (vals != null) {
            Preconditions.checkState(vals.length == 1,
                Logger.format("Requested a single value parameter, but there were multiple values",
                    "name", name, "values", vals));
            return vals[0];
        }
        return def;
    }

    /**
     * Returns the value for the parameter on this request as converted by <code>converter</code>
     * or <code>def</code>if it has no such parameter.<p>
     *
     * If <code>converter</code> can't handle the value and throws
     * <code>ConversionFailedException</code>, that will be propagated by this method with the
     * parameter name and value filled in. Any other exceptions thrown by <code>converter</code>
     * will be wrapped in <code>ConversionFailedException</code> and rethrown.<p>
     *
     * IllegalStateException is thrown if the request has multiple values for the parameter.
     */
    public <T> T get (String name, Function<String, T> converter, T def)
    {
        String val = get(name);
        if (val != null) {
            return convert(converter, name, val);
        }
        return def;
    }

    /**
     * Returns the value for the parameter on this request or throws
     * <code>NullPointerException</code> if it has no such parameter. IllegalStateException is
     * thrown if the request has multiple values for the parameter.
     */
    public String require (String name)
    {
        String val = get(name);
        Preconditions.checkNotNull(val, "'" + name + "' was required but not present");
        return val;
    }

    /**
     * Returns the value for the parameter on this request as converted by <code>converter</code>
     * or throws <code>NullPointerException</code> if it has no such parameter.<p>
     *
     * If <code>converter</code> can't handle the value and throws
     * <code>ConversionFailedException</code>, that will be propagated by this method with the
     * parameter name and value filled in. Any other exceptions thrown by <code>converter</code>
     * will be wrapped in <code>ConversionFailedException</code> and rethrown.<p>
     *
     * IllegalStateException is thrown if the request has multiple values for the parameter.
     */
    public <T> T require (String name, Function<String, T> converter)
    {
        return convert(converter, name, require(name));
    }

    /**
     * Returns all the values for the given parameter on the request, or <code>defaultValues</code>
     * if it has no such parameter.
     */
    public String[] getAll (String name, String... defaultValues)
    {
        String[] vals = _req.getParameterValues(name);
        if (vals == null) {
            return defaultValues;
        }
        return vals;
    }

    /**
     * Returns all the values for the given parameter on the request as converted by
     * <code>converter</code>, or <code>defaultValues</code> if it has no such parameter.<p>
     *
     * If <code>converter</code> can't handle the value and throws
     * <code>ConversionFailedException</code>, that will be propagated by this method with the
     * parameter name and value filled in. Any other exceptions thrown by <code>converter</code>
     * will be wrapped in <code>ConversionFailedException</code> and rethrown.
     */
    public <T> Collection<T> getAll (final String name, final Function<String, T> converter,
        T... defaultValues)
    {
        String[] vals = _req.getParameterValues(name);
        if (vals == null) {
            return Arrays.asList(defaultValues);
        }
        return Lists.transform(Arrays.asList(vals), new Function<String, T>(){
            public T apply (String from) {
                return convert(converter, name, from);
            }});
    }

    /**
     * Returns the result of passing the given value into converter. If an exception is thrown by
     * it, a ConversionFailedException is thrown with the name and value filled in as part of its
     * message.
     */
    protected static <T> T convert (Function<String, T> converter, String name, String val)
    {
        try {
            return converter.apply(val);
        } catch (RuntimeException re) {
            ConversionFailedException fail;
            if (re instanceof ConversionFailedException) {
                fail = (ConversionFailedException)re;
            } else {
                fail = new ConversionFailedException(re, "Failed converting parameter");
            }
            fail.append("name", name, "value", val);
            throw fail;
        }
    }

    protected final ServletRequest _req;
}
