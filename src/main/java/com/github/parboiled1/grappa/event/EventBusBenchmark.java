package com.github.parboiled1.grappa.event;

import com.github.parboiled1.grappa.event.guava.GuavaEventBusParser;
import com.github.parboiled1.grappa.event.guava.GuavaMatchListener;
import com.github.parboiled1.grappa.event.mbassador.MbassadorMatchListener;
import com.github.parboiled1.grappa.event.mbassador.MbassadorEventBusParser;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;

public final class EventBusBenchmark
{
    private static final String INPUT = "aaaaaaaaa";

    private final ParseRunner<Object> guavaRunner;
    private final ParseRunner<Object> mbassadorRunner;

    public EventBusBenchmark()
    {
        final GuavaEventBusParser guavaParser = Parboiled.createParser(
            GuavaEventBusParser.class, new GuavaMatchListener());
        final MbassadorEventBusParser mbassadorParser = Parboiled.createParser(
            MbassadorEventBusParser.class, new MbassadorMatchListener());

        guavaRunner = new BasicParseRunner<>(guavaParser.rule());
        mbassadorRunner = new BasicParseRunner<>(mbassadorParser.rule());
    }

    @Benchmark
    public void guavaEventBus(final int reps)
    {
        for (int i = 0; i < reps; i++)
            guavaRunner.run(INPUT);
    }

    @Benchmark
    public void mbassadorEventBus(final int reps)
    {
        for (int i = 0; i < reps; i++)
            mbassadorRunner.run(INPUT);
    }

    public static void main(final String... args)
    {
        CaliperMain.main(EventBusBenchmark.class, args);
    }
}
