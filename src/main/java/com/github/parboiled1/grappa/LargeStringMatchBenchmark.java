package com.github.parboiled1.grappa;

import com.github.parboiled1.grappa.matchers.trie.Trie;
import com.github.parboiled1.grappa.matchers.trie.TrieBuilder;
import com.github.parboiled1.grappa.matchers.trie.TrieMatcher;
import com.github.parboiled1.grappa.util.MatcherContextBuilder;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.matchers.FirstOfStringsMatcher;
import org.parboiled.matchers.Matcher;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class LargeStringMatchBenchmark
{
    private static final Comparator<String> CMP = new Comparator<String>()
    {
        @Override
        public int compare(final String o1, final String o2)
        {
            return o2.compareTo(o1);
        }
    };

    private static final String[] WORDS_ARRAY;
    private static final List<String> WORDS_LIST;
    private static final Matcher FIRST_OF_STRINGS;
    private static final Matcher TRIE;

    static {
        try {
            final Path path = Paths.get("/usr/share/dict/words");
            final Charset cs = StandardCharsets.UTF_8;
            final Set<String> set = new TreeSet<>(CMP);

            for (final String line: Files.readAllLines(path, cs))
                if (line.length() >= 2)
                    set.add(line);

            WORDS_LIST = ImmutableList.copyOf(set);
            WORDS_ARRAY = WORDS_LIST.toArray(new String[WORDS_LIST.size()]);

            final Stopwatch stopwatch = Stopwatch.createStarted();

            FIRST_OF_STRINGS = new FirstOfStringsMatcher(new Rule[0],
                toCharArrays(WORDS_ARRAY));
            final TrieBuilder builder = Trie.newBuilder();
            for (final String word: WORDS_ARRAY)
                if (word.length() >= 2)
                    builder.addWord(word);
            TRIE = new TrieMatcher(builder.build());

            stopwatch.stop();
            System.out.println("DONE generating matchers; time: " + stopwatch);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Param({
        "fubar", "innuendo", "cétacé", "assimilate", "bergynion",
        "castration", "Drizzt Do'Urden", "holiday", "abstractions",
        "glaring omission", "Oppenheimer", "cleaver", "desiderata",
        "endurance", "Palermo", "procrastination", "engulfed", "grappa",
        "forelorn", "dimwit", "nonproblem", "overflow", "algorithm"
    })
    String input;

    @Param({"FIRST_OF_STRINGS", "TRIE"})
    String matcherType;

    @Benchmark
    public boolean stringMatch(final int rep)
    {
        final Matcher matcher = "TRIE".equals(matcherType)
            ? TRIE : FIRST_OF_STRINGS;
        boolean ret = true;
        final MatcherContext<Object> context = new MatcherContextBuilder()
            .withInput(input).withMatcher(matcher).build();
        for (int i = 0; i < rep; i++) {
            ret = matcher.match(context);
            context.setCurrentIndex(0);
        }
        return ret;
    }

    public static void main(final String... args)
    {
        CaliperMain.main(LargeStringMatchBenchmark.class,
            new String[] {});
    }

    private static char[][] toCharArrays(final String[] strings)
    {
        final int size = strings.length;
        final char[][] ret = new char[size][];
        for (int i = 0; i < size; i++)
            ret[i] = strings[i].toCharArray();
        return ret;
    }
}
