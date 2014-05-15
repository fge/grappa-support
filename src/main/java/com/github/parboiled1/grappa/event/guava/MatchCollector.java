package com.github.parboiled1.grappa.event.guava;

import com.google.common.eventbus.Subscribe;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;

public final class MatchCollector
{
    private String match;

    @Subscribe
    public void matchEvent(final MatchEvent event)
    {
        match = event.getMatched();
    }

    public String getMatch()
    {
        return match;
    }

    public static void main(final String... args)
    {
        final MatchCollector collector = new MatchCollector();

        final GuavaEventBusParser parser
            = Parboiled.createParser(GuavaEventBusParser.class, collector);

        final ParseRunner<Object> runner
            = new BasicParseRunner<>(parser.rule());

        runner.run("aaaaaaaaaa");

        System.out.println(collector.getMatch());
    }
}
