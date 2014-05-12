package com.github.parboiled1.grappa;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import java.nio.CharBuffer;
import java.util.Comparator;
import java.util.Set;

public final class BloomMatcherBuilder
{
    private static final Comparator<CharSequence> CHARSEQUENCE_REVERSE
        = new Comparator<CharSequence>()
    {
        @Override
        public int compare(final CharSequence o1, final CharSequence o2)
        {
            return CharBuffer.wrap(o2).compareTo(CharBuffer.wrap(o1));
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

    private final SortedSetMultimap<Integer, CharSequence> stringMap
        = TreeMultimap.create(INTEGER_REVERSE, CHARSEQUENCE_REVERSE);
    private BloomFilter<CharSequence> bloomFilter;
    private int length;

    public BloomMatcherBuilder withStrings(@Nonnull final Set<String> strings)
    {
        bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(),
            strings.size());

        int maxLen = 0;
        for (final String string: strings) {
            maxLen = Math.max(maxLen, string.length());
            stringMap.put(string.length(), string);
            bloomFilter.put(string);
        }
        length = maxLen;
        return this;
    }

    public BloomFilterStringMatcher build()
    {
        return new BloomFilterStringMatcher(bloomFilter, stringMap, length);
    }

    public static void main(final String... args)
    {
        final CharBuffer buf1 = CharBuffer.wrap("foobar");
        final CharBuffer buf2 = CharBuffer.wrap("foobar");

        System.out.println(buf1.equals(buf2));
        System.out.println(buf1.equals(buf2));
        buf1.limit(3);
        System.out.println(buf1.equals(buf2));
    }
}
