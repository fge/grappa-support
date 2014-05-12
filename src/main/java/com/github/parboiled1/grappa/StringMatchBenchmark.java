package com.github.parboiled1.grappa;

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

public final class StringMatchBenchmark
{
    /*
     * From http://en.wikipedia.org/wiki/List_of_Java_keywords
     *
     * Note that longest keywords come first for FirstOfStringsMatcher which
     * does not like for instance "do" before "double".
     */
    private static final String[] KEYWORDS = {
        "abstract", "assert", "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default", "double", "do",
        "else", "enum", "extends", "finally", "final", "float", "for",
        "goto", "if", "implements", "import", "instanceof", "interface", "int",
        "long", "native", "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super", "switch",
        "synchronized", "this", "throws", "throw", "transient", "try", "void",
        "volatile", "while", "false", "null", "true"
    };

    @Param({
        "abstrac", "int", "asser", "while", "strictfp", "for", "if",
        "package", "syncronized", "transient", "volatil", "double", "do",
        "inport", "else", "continua", "finall", "instanceof"
    }) String input;

    private Matcher firstOfStrings;
    private Matcher bloom;

    @BeforeExperiment
    public void initMatchers()
    {
        firstOfStrings = new FirstOfStringsMatcher(new Rule[0],
            toCharArrays(KEYWORDS));
        bloom = new BloomMatcherBuilder()
            .withStrings(ImmutableSet.copyOf(KEYWORDS)).build();
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
        CaliperMain.main(StringMatchBenchmark.class, args);
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
