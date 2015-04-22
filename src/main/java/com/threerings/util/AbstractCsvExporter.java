//
// $Id: AbstractCsvExporter.java 36591 2014-03-18 17:54:12Z ray $

package com.threerings.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

import static com.threerings.util.Log.log;

/**
 * Abstract class for exporting information to csv.
 */
public abstract class AbstractCsvExporter
{
    /**
     * The details of an individual row.
     */
    public interface Details<T extends Details<?>>
        extends Comparable<T>
    {
        public List<Object> toRow ();
    }

    /**
     * Export the rows to a file.
     */
    protected static void export (
        File file, List<? extends Object> headers, Iterable<? extends Details<?>> rows)
    {
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file), true, "UTF-8");
            ps.println(toCsv(headers));
            for (Details<?> row : Ordering.natural().immutableSortedCopy(rows)) {
                ps.println(toCsv(row.toRow()));
            }
            ps.close();
        } catch (IOException ioe) {
            log.warning("Error creating file.", "file", file, ioe);
        }
    }

    /**
     * Convert the row to csv.
     */
    protected static String toCsv (List<? extends Object> row)
    {
        return Joiner.on(',').join(
            Iterables.transform(
                row,
                new Function<Object, Object>() {
                    public Object apply (Object o) {
                        if ((o == null) || (o instanceof Number) || (o instanceof Boolean)) {
                            return o;
                        }
                        // quotes need to be escaped by doubling them
                        return "\"" + CharMatcher.is('"').replaceFrom(String.valueOf(o), "\"\"") +
                            "\"";
                    }
                }));
    }
}
