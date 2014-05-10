package com.github.parboiled1.grappa;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.ArrayList;
import java.util.List;

public final class TestBenchmark
{
    private static final int COUNT = 100_000;
    private static final List<Integer> LIST = new ArrayList<>();

    static {
        for (int i = 0; i < COUNT; i++)
            LIST.add(i);
    }

    @GenerateMicroBenchmark
    public void foreachLoop()
    {
        int dummy;
        for (final int i: LIST)
            dummy = i;
    }

    @GenerateMicroBenchmark
    public void forLoop()
    {
        int dummy;
        final int size = LIST.size();
        for (int i = 0; i < size; i++)
            dummy = LIST.get(i);
    }

    public static void main(final String... args)
        throws RunnerException
    {
        // Doesn't work. And jmh has VERY poor documentation.
        // For one, no hint as to how NOT to use the file in META-INF.
        final Options options = new OptionsBuilder()
            .forks(1)
            .warmupIterations(1)
            .measurementIterations(20)
            .verbosity(VerboseMode.EXTRA)
            .build();

        new Runner(options).run();
    }
}
