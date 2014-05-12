package com.github.parboiled1.grappa;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.hash.BloomFilter;
import org.parboiled.MatcherContext;
import org.parboiled.buffers.InputBuffer;
import org.parboiled.matchers.AbstractMatcher;
import org.parboiled.matchervisitors.MatcherVisitor;

import javax.annotation.Nonnull;
import java.util.Set;

public final class BloomFilterStringMatcher
    extends AbstractMatcher
{
    private final int minlength;
    private final BloomFilter<CharSequence> bloomFilter;
    private final SetMultimap<String, String> multimap;

    public BloomFilterStringMatcher(
        @Nonnull final BloomFilter<CharSequence> bloomFilter,
        @Nonnull final SetMultimap<String, String> multimap)
    {
        super("Bloom: " + Preconditions.checkNotNull(multimap).size()
            + " strings");
        this.bloomFilter = Preconditions.checkNotNull(bloomFilter);
        this.multimap = ImmutableSetMultimap.copyOf(multimap);
        minlength = multimap.keySet().iterator().next().length();
    }

    /**
     * Tries a match on the given MatcherContext.
     *
     * @param context the MatcherContext
     * @return true if the match was successful
     */
    @Override
    public <V> boolean match(final MatcherContext<V> context)
    {
        final InputBuffer buffer = context.getInputBuffer();
        int index = context.getCurrentIndex();
        final String prefix  = buffer.extract(index, index + minlength);
        if (!bloomFilter.mightContain(prefix))
            return false;
        index += minlength;
        final Set<String> set = multimap.get(prefix);

        char[] chars;
        for (final String suffix: set) {
            if (!buffer.test(index, suffix.toCharArray()))
                continue;
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
