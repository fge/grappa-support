package com.github.parboiled1.grappa.util;

import com.github.parboiled1.grappa.stack.DefaultValueStack;
import com.google.common.base.Preconditions;
import org.parboiled.MatcherContext;
import org.parboiled.buffers.CharSequenceInputBuffer;
import org.parboiled.buffers.InputBuffer;
import org.parboiled.errors.ParseError;
import org.parboiled.matchers.Matcher;
import org.parboiled.support.ValueStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class MatcherContextBuilder
{
    private static final ValueStack<Object> STACK = new DefaultValueStack<>();
    private static final List<ParseError> ERRORS = new ArrayList<>();

    private InputBuffer buffer = null;
    private boolean fastStringMatching = true;
    private Matcher matcher = null;

    public MatcherContextBuilder withInput(@Nonnull final String input)
    {
        buffer = new CharSequenceInputBuffer(input);
        return this;
    }

    public MatcherContextBuilder withFastStringMatching(final boolean fsm)
    {
        fastStringMatching = fsm;
        return this;
    }

    public MatcherContextBuilder withMatcher(@Nonnull final Matcher matcher)
    {
        this.matcher = Preconditions.checkNotNull(matcher);
        return this;
    }

    public MatcherContext<Object> build()
    {
        return new MatcherContext<>(buffer, STACK, ERRORS,
            SimpleMatchHandler.INSTANCE, matcher, fastStringMatching);
    }
}
