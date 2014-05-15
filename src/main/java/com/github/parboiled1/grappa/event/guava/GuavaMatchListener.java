package com.github.parboiled1.grappa.event.guava;

import com.github.parboiled1.grappa.event.MatchEvent;
import com.google.common.eventbus.Subscribe;

public final class GuavaMatchListener
{
    @Subscribe
    public void matchEvent(final MatchEvent event)
    {
    }
}
