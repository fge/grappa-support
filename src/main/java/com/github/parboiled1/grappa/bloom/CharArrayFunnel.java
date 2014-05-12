package com.github.parboiled1.grappa.bloom;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public final class CharArrayFunnel
    implements Funnel<char[]>
{
    public static final Funnel<char[]> INSTANCE
        = new CharArrayFunnel();

    private CharArrayFunnel()
    {
    }

    /**
     * Sends a stream of data from the {@code from} object into the sink
     * {@code into}. There
     * is no requirement that this data be complete enough to fully
     * reconstitute the object
     * later.
     *
     * @param from
     * @param into
     * @since 12.0 (in Guava 11.0, {@code PrimitiveSink} was named {@code Sink})
     */
    @Override
    public void funnel(final char[] from, final PrimitiveSink into)
    {
        for (final char c: from)
            into.putChar(c);
    }
}
