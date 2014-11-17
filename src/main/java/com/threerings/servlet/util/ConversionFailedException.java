//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import com.samskivert.util.LogBuilder;

/**
 * Indicates that a converter couldn't handle a parameter value in {@link Parameters}.
 */
public class ConversionFailedException extends RuntimeException
{
    public ConversionFailedException (Throwable cause)
    {
        this(cause, "");
    }

    /**
     * Creates an exception message with the given base message and key value pairs as formatted
     * by {@link LogBuilder}.
     */
    public ConversionFailedException (Throwable cause, Object msg, Object...args)
    {
        this(msg, args);
        initCause(cause);
    }

    public ConversionFailedException (Object msg, Object...args)
    {
        _builder = new LogBuilder(msg, args);
    }

    public ConversionFailedException ()
    {
        this("");
    }

    /**
     * Adds the given key value pairs to the message.
     */
    public void append (Object... args)
    {
        _builder.append(args);
    }

    @Override
    public String getMessage ()
    {
        return _builder.toString();
    }

    protected final LogBuilder _builder;
}
