package com.github.parboiled1.grappa.event.mbassador;

import com.github.parboiled1.grappa.event.MatchEvent;
import net.engio.mbassy.listener.Handler;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;

public final class MbassadorMatchListener
{
    private String match;

    @Handler
    public void handleMatchEvent(final MatchEvent event)
    {
        match = event.getMatched();
    }

    public String getMatch()
    {
        return match;
    }

    public static void main(final String... args)
    {
        final MbassadorMatchListener listener = new MbassadorMatchListener();

        final MbassadorEventBusParser parser
            = Parboiled.createParser(MbassadorEventBusParser.class, listener);

        final ParseRunner<Object> runner
            = new BasicParseRunner<>(parser.rule());

        runner.run("aaaaaaaaaa");

        System.out.println(listener.getMatch());
    }
}
