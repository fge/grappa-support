package com.github.parboiled1.grappa.bloom;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.hash.BloomFilter;
import org.parboiled.MatcherContext;
import org.parboiled.matchers.AbstractMatcher;
import org.parboiled.matchervisitors.MatcherVisitor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.CharBuffer;
import java.util.Arrays;

import static com.google.common.base.Equivalence.Wrapper;

/**
 * A multi-string matcher based on a {@link BloomFilter}
 *
 * <p>This matcher will test for the longest strings first.</p>
 *
 * <p>There are three arguments to the constructor: the bloom filter itself,
 * preloaded with all the strings; a {@link SetMultimap} containing string
 * lengths (in the reverse order) as keys, and matching strings as values;
 * and the precomputed maximum string length.</p>
 *
 * <p>See the details of matching in {@link #match(MatcherContext)}. Please note
 * that the process relies on the fact that {@link CharBuffer} implements
 * {@link CharSequence}.</p>
 */
@ParametersAreNonnullByDefault
public final class BloomFilterStringMatcher
    extends AbstractMatcher
{
    private final BloomFilter<char[]> bloomFilter;
    private final SetMultimap<Integer, Wrapper<char[]>> stringMap;
    private final int length;

    public BloomFilterStringMatcher(final BloomFilter<char[]> bloomFilter,
        final SetMultimap<Integer, Wrapper<char[]>> stringMap, final int length)
    {
        super("Bloom: " + stringMap.size() + " strings");
        this.length = length;
        this.stringMap = ImmutableSetMultimap.copyOf(stringMap);
        this.bloomFilter = bloomFilter;
    }

    /**
     * Tries a match on the given MatcherContext.
     *
     * <p>First, a string of the maximum possible length is extracted from
     * the input buffer and wrapped into a {@link CharBuffer}. Then the
     * process is as follows:</p>
     *
     * <ul>
     *      <li>walk the {@code SetMultimap} in order; therefore we have the
     *      greatest lengths coming first;</li>
     *      <li>set the limit of the extracted buffer to the length;</li>
     *      <li>try and match against the bloomfilter;</li>
     *      <li>only if {@link BloomFilter#mightContain(Object)} returns true,
     *      search the set of strings of this length for a match;</li>
     *      <li>if the string is in the set, we have a match: set the index,
     *      create the parsing node and exit.</li>
     * </ul>
     *
     * <p>If we walk all the {@code SetMultimap} without having found a match,
     * the match fails.</p>
     *
     * @param context the MatcherContext
     * @return true if the match was successful
     */
    @Override
    public <V> boolean match(final MatcherContext<V> context)
    {
        final int index = context.getCurrentIndex();
        final String s
            = context.getInputBuffer().extract(index, index + length);
        final char[] chars = s.toCharArray();
        final int len = chars.length;

        char[] tested;

        for (final int testedLength: stringMap.keySet()) {
            if (testedLength > len)
                continue;
            tested = Arrays.copyOf(chars, testedLength);
            if (!bloomFilter.mightContain(tested))
                continue;
            if (!stringMap.get(testedLength)
                .contains(CharArrayEquivalence.INSTANCE.wrap(tested)))
                continue;
            context.advanceIndex(testedLength);
            context.createNode();
            return true;
        }
        return false;
    }

    /**
     * Accepts the given matcher visitor.
     *
     * @param visitor the visitor
     * @return the value returned by the given visitor
     */
    @Override
    public <R> R accept(final MatcherVisitor<R> visitor)
    {
        // TODO
        return null;
    }
}
