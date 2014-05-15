package com.github.parboiled1.grappa.event.guava;

import com.google.common.eventbus.EventBus;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

import javax.annotation.Nonnull;

public class GuavaEventBusParser
    extends BaseParser<Object>
{
    private final EventBus eventBus = new EventBus();

    public GuavaEventBusParser(@Nonnull final MatchCollector collector)
    {
        eventBus.register(collector);
    }

    Rule rule()
    {
        return sequence(oneOrMore('a'), event());
    }

    boolean event()
    {
        final MatchEvent event = new MatchEvent(getContext().getMatch());
        eventBus.post(event);
        return true;
    }
}

