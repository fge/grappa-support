package com.github.parboiled1.grappa;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class BloomMatcherBuilder
{
    private static final Comparator<String> STRINGLEN
        = new Comparator<String>()
    {
        @Override
        public int compare(final String o1, final String o2)
        {
            // Java 6 does not have Integer.compare()
            return Ints.compare(o1.length(), o2.length());
        }
    };

    private List<String> strings;
    private final ListMultimap<String, String> stringMap
        = ArrayListMultimap.create();
    private BloomFilter<CharSequence> bloomFilter;

    public BloomMatcherBuilder withStrings(@Nonnull final List<String> strings)
    {
        final SortedSet<String> set = new TreeSet<>();
        set.addAll(strings);
        this.strings = new ArrayList<>(set);
        Collections.reverse(this.strings);
        buildMultimap();
        return this;
    }

    private void buildMultimap()
    {
        final SortedSet<String> treeSet = new TreeSet<>(STRINGLEN);
        treeSet.addAll(strings);
        final int minlen = treeSet.iterator().next().length();

        // Second pass, insert in multimap
        for (final String s: strings)
            stringMap.put(s.substring(0, minlen), s.substring(minlen));

        final Set<String> set = stringMap.keySet();
        bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(),
            set.size());

        for (final String key: set)
            bloomFilter.put(key);
    }
}
