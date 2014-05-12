package com.github.parboiled1.grappa.bloom;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.hash.BloomFilter;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import java.nio.CharBuffer;
import java.util.Comparator;
import java.util.Set;

import static com.google.common.base.Equivalence.Wrapper;

public final class BloomMatcherBuilder
{
    private static final Comparator<Wrapper<char[]>> CHARARRAY_COMPARATOR
        = new Comparator<Wrapper<char[]>>()
    {
        @Override
        public int compare(final Wrapper<char[]> o1,
            final Wrapper<char[]> o2)
        {
            return CharBuffer.wrap(o2.get())
                .compareTo(CharBuffer.wrap(o1.get()));
        }
    };

    private static final Comparator<Integer> INTEGER_REVERSE
        = new Comparator<Integer>()
    {
        @Override
        public int compare(final Integer o1, final Integer o2)
        {
            return Ints.compare(o2, o1);
        }
    };

    private final SortedSetMultimap<Integer, Wrapper<char[]>> stringMap
        = TreeMultimap.create(INTEGER_REVERSE, CHARARRAY_COMPARATOR);
    private int length;
    private double fpp = 0.03; // default

    public BloomMatcherBuilder withStrings(@Nonnull final Set<String> strings)
    {
        int maxLen = 0;
        char[] chars;
        for (final String string: strings) {
            maxLen = Math.max(maxLen, string.length());
            chars = string.toCharArray();
            stringMap.put(string.length(),
                CharArrayEquivalence.INSTANCE.wrap(chars));
        }
        length = maxLen;
        return this;
    }

    public BloomMatcherBuilder withFpp(final double fpp)
    {
        this.fpp = fpp;
        return this;
    }

    public BloomFilterStringMatcher build()
    {
        final BloomFilter<char[]> bloomFilter
            = BloomFilter.create(CharArrayFunnel.INSTANCE, stringMap.size(),
            fpp);

        for (final Wrapper<char[]> chars: stringMap.values())
            //noinspection ConstantConditions
            bloomFilter.put(chars.get());

        return new BloomFilterStringMatcher(bloomFilter, stringMap, length);
    }
}
