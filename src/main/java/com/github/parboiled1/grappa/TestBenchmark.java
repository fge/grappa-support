package com.github.parboiled1.grappa;

import com.google.caliper.Benchmark;
import com.google.caliper.config.InvalidConfigurationException;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.runner.InvalidBenchmarkException;
import com.google.caliper.util.InvalidCommandException;

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
    public void foreachLoop()
    {
        int dummy;
        for (final int i: LIST)
            dummy = i;
    }

    @Benchmark
    public void forLoop()
    {
        int dummy;
        final int size = LIST.size();
        for (int i = 0; i < size; i++)
            dummy = LIST.get(i);
    }

    public static void main(final String... args)
        throws InvalidConfigurationException, InvalidCommandException,
        InvalidBenchmarkException
    {
        CaliperMain.main(TestBenchmark.class, args);
    }
}
