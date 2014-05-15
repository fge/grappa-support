package com.github.parboiled1.grappa.event.guava;

public final class MatchEvent
{
    private final String matched;

    public MatchEvent(final String matched)
    {
        this.matched = matched;
    }

    public String getMatched()
    {
        return matched;
    }
}
