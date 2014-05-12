package com.github.parboiled1.grappa;

import com.github.parboiled1.grappa.trie.Trie;
import com.github.parboiled1.grappa.trie.TrieStringMatcher;
import com.github.parboiled1.grappa.util.MatcherContextBuilder;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;
import com.google.common.base.Stopwatch;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.matchers.FirstOfStringsMatcher;
import org.parboiled.matchers.Matcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class LargeStringMatchBenchmark
{
    private static final String[] WORDS_ARRAY;
    private static final List<String> WORDS_LIST;
    private static final Matcher FIRST_OF_STRINGS;
    private static final Matcher TRIE;

    static {
        try {
            final Path path = Paths.get("/usr/share/dict/words");
            WORDS_LIST = Files.readAllLines(path,
                StandardCharsets.UTF_8);
            Collections.sort(WORDS_LIST, new Comparator<String>()
            {
                @Override
                public int compare(final String o1, final String o2)
                {
                    return o2.compareTo(o1);
                }
            });
            WORDS_ARRAY = WORDS_LIST.toArray(new String[WORDS_LIST.size()]);
            final Stopwatch stopwatch = Stopwatch.createStarted();
            FIRST_OF_STRINGS = new FirstOfStringsMatcher(new Rule[0],
                toCharArrays(WORDS_ARRAY));
            final Trie.Builder builder = Trie.newBuilder();
            for (final String word: WORDS_ARRAY)
                builder.addWord(word);
            TRIE = new TrieStringMatcher(builder.build());
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
            new String[] { "-i", "runtime" });
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
