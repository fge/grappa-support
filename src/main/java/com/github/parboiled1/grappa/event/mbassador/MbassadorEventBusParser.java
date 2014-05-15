package com.github.parboiled1.grappa.event.mbassador;

import com.github.parboiled1.grappa.event.MatchEvent;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

import javax.annotation.Nonnull;

public class MbassadorEventBusParser
    extends BaseParser<Object>
{
    private final MBassador<MatchEvent> bus
        = new MBassador<>(BusConfiguration.Default());

    public MbassadorEventBusParser(
        @Nonnull final MbassadorMatchListener listener)
    {
        bus.subscribe(listener);
    }

    public Rule rule()
    {
        return sequence("aaaaaaaaa", event());
    }

    boolean event()
    {
        final MatchEvent event = new MatchEvent(getContext().getMatch());
        bus.post(event).now();
        return true;
    }
}

