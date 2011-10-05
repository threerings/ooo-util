//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.servlet.util;

import com.samskivert.util.Logger;

/**
 * Contains a reference to the log object used by this package.
 */
class Log
{
    /** We dispatch our log messages through this logger. */
    public static Logger log = Logger.getLogger("ooo-servlet-util");
}
