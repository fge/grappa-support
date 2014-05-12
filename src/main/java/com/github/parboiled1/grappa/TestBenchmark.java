package com.github.parboiled1.grappa;

import com.github.parboiled1.grappa.util.MatcherContextBuilder;
import com.google.caliper.Benchmark;
import com.google.caliper.config.InvalidConfigurationException;
import com.google.caliper.runner.InvalidBenchmarkException;
import com.google.caliper.util.InvalidCommandException;
import com.google.common.collect.ImmutableList;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.matchers.FirstOfStringsMatcher;

import java.util.ArrayList;
import java.util.List;

public final class TestBenchmark
{
    private static final int COUNT = 1_000_000;
    private static final List<Integer> LIST = new ArrayList<>();

    static {
        for (int i = 0; i < COUNT; i++)
            LIST.add(i);
    }

    @Benchmark
    public int foreachLoop()
    {
        int dummy = 0;
        for (final int i: LIST)
            dummy = i;
        return dummy;
    }

    @Benchmark
    public int forLoop()
    {
        int dummy = 0;
        final int size = LIST.size();
        for (int i = 0; i < size; i++)
            dummy = LIST.get(i);
        return dummy;
    }

    public static void main(final String... args)
        throws InvalidConfigurationException, InvalidCommandException,
        InvalidBenchmarkException
    {
        final List<String> strings = ImmutableList.of("hello", "world");
        final FirstOfStringsMatcher matcher
            = new FirstOfStringsMatcher(new Rule[0], toCharArrays(strings));
        final MatcherContext<Object> context = new MatcherContextBuilder()
            .withFastStringMatching(true).withInput("hellos")
            .withMatcher(matcher).build();
        System.out.println(matcher.match(context));
        //CaliperMain.main(TestBenchmark.class, args);
    }

    private static char[][] toCharArrays(final List<String> strings)
    {
        final int size = strings.size();
        final char[][] ret = new char[size][];
        for (int i = 0; i < size; i++)
            ret[i] = strings.get(i).toCharArray();
        return ret;
    }
}
