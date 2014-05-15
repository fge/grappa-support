package com.github.parboiled1.grappa.event.guava;

import com.github.parboiled1.grappa.event.MatchEvent;
import com.google.common.eventbus.EventBus;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

import javax.annotation.Nonnull;

public class GuavaEventBusParser
    extends BaseParser<Object>
{
    private final EventBus bus = new EventBus();

    public GuavaEventBusParser(@Nonnull final GuavaMatchListener collector)
    {
        bus.register(collector);
    }

    public Rule rule()
    {
        return sequence("aaaaaaaaa", event());
    }

    boolean event()
    {
        final MatchEvent event = new MatchEvent(getContext().getMatch());
        bus.post(event);
        return true;
    }
}

