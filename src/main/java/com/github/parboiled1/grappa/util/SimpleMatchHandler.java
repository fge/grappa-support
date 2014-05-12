package com.github.parboiled1.grappa.util;

import org.parboiled.MatchHandler;
import org.parboiled.MatcherContext;

public enum SimpleMatchHandler
    implements MatchHandler
{
    INSTANCE
    {
        /**
         * Runs the given MatcherContext.
         *
         * @param context the MatcherContext
         * @return true if matched
         */
        @Override
        public boolean match(final MatcherContext<?> context)
        {
            return context.getMatcher().match(context);
        }
    }
}
