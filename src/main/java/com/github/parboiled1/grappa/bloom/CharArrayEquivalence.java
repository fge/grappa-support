package com.github.parboiled1.grappa.bloom;

import com.google.common.base.Equivalence;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public final class CharArrayEquivalence
    extends Equivalence<char[]>
{
    public static final Equivalence<char[]> INSTANCE
        = new CharArrayEquivalence();

    private CharArrayEquivalence()
    {
    }

    /**
     * Returns {@code true} if {@code a} and {@code b} are considered
     * equivalent.
     * <p>Called by {@link #equivalent}. {@code a} and {@code b} are not the
     * same
     * object and are not nulls.
     *
     * @param a
     * @param b
     * @since 10.0 (previously, subclasses would override equivalent())
     */
    @Override
    protected boolean doEquivalent(final char[] a, final char[] b)
    {
        return Arrays.equals(a, b);
    }

    /**
     * Returns a hash code for non-null object {@code t}.
     * <p>Called by {@link #hash}.
     *
     * @param t
     * @since 10.0 (previously, subclasses would override hash())
     */
    @Override
    protected int doHash(final char[] t)
    {
        return Arrays.hashCode(t);
    }
}
