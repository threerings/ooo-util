//
// ooo-util - a place for OOO utilities
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/ooo-util/blob/master/LICENSE

package com.threerings.util;

import java.util.Properties;

import com.samskivert.util.Config;

import com.samskivert.jdbc.ConnectionProvider;
import com.samskivert.jdbc.StaticConnectionProvider;

public class ConnectionProviderUtil
{
    public enum DB {
        postgres, hsqldb;
    }

    /**
     * Creates a ConnectionProvider for the type of databases specified by <code>db.type</code> in
     * <code>config</code>.
     * <h2>Types</h2>
     * <dl>
     * <dt>postgres</dt>
     * <dd>Uses {@link PostgresUtil#createPoolingProvider(Config, String)} with the given config
     * and ident.</dd>
     * <dt>hsqldb</dt>
     * <dd>a {@link StaticConnectionProvider} is created from
     * the sub-properties under <code>db</code>.  It defaults to an in-memory database if
     * <code>db.default.url</code> isn't defined.
     * </dd>
     * </dl>
     *
     * @throws IllegalArgumentException - if <code>db.type</code> doesn't match a known type.
     */
    public static ConnectionProvider createProvider (Config config, String ident)
    {
        return createProvider(config, ident, "db");
    }

    /**
     * Creates a ConnectionProvider as in {@link #createProvider(Config,String)}, but uses
     * the supplied {@code prefix} in place of {@code db}.
     */
    public static ConnectionProvider createProvider (Config config, String ident, String prefix)
    {
        DB type = DB.valueOf(config.getValue(prefix + ".type", "postgres"));
        switch(type) {
        case postgres:
            return PostgresUtil.createPoolingProvider(config, ident, prefix);
        case hsqldb:
            Properties sub = config.getSubProperties(prefix);
            putIfNotPresent(sub, "default.driver", "org.hsqldb.jdbcDriver");
            putIfNotPresent(sub, "default.url", "jdbc:hsqldb:mem:.");
            putIfNotPresent(sub, "default.username", "sa");
            putIfNotPresent(sub, "default.password", "");
            return new StaticConnectionProvider(sub);
        default:
        throw new IllegalArgumentException("Unknown db type " +
            config.getValue(prefix + ".type", (String)null));
        }
    }

    protected static void putIfNotPresent (Properties props, String key, String value)
    {
        if (!props.containsKey(key)) {
            props.put(key, value);
        }
    }

}
