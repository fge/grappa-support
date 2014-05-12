package com.github.parboiled1.grappa;

import com.github.parboiled1.grappa.bloom.BloomMatcherBuilder;
import com.github.parboiled1.grappa.util.MatcherContextBuilder;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;
import com.google.common.collect.ImmutableSet;
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
    }) String input;

    private Matcher firstOfStrings;
    private Matcher bloom;

    @BeforeExperiment
    public void initMatchers()
    {
        firstOfStrings = new FirstOfStringsMatcher(new Rule[0],
            toCharArrays(WORDS_ARRAY));
        bloom = new BloomMatcherBuilder()
            .withStrings(ImmutableSet.copyOf(WORDS_LIST)).build();
    }

    @Benchmark
    public boolean usingFirstOfStrings(final int rep)
    {
        boolean ret = true;
        final MatcherContext<Object> context = new MatcherContextBuilder()
            .withInput(input).withMatcher(firstOfStrings).build();
        for (int i = 0; i < rep; i++) {
            ret = firstOfStrings.match(context);
            context.setCurrentIndex(0);
        }
        return ret;
    }

    @Benchmark
    public boolean usingBloom(final int rep)
    {
        boolean ret = true;
        final MatcherContext<Object> context = new MatcherContextBuilder()
            .withInput(input).withMatcher(bloom).build();
        for (int i = 0; i < rep; i++) {
            ret = bloom.match(context);
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
