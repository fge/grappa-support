package com.github.parboiled1.grappa.trie;

import org.parboiled.MatcherContext;
import org.parboiled.matchers.AbstractMatcher;
import org.parboiled.matchervisitors.MatcherVisitor;

public final class TrieStringMatcher
    extends AbstractMatcher
{
    private final Trie trie;

    public TrieStringMatcher(final Trie trie)
    {
        super("Trie");
        this.trie = trie;
    }

    /**
     * Tries a match on the given MatcherContext.
     *
     * @param context the MatcherContext
     * @return true if the match was successful
     */
    @Override
    public <V> boolean match(final MatcherContext<V> context)
    {
        final int maxLength = trie.getMaxLength();
        final int index = context.getCurrentIndex();
        final String input = context.getInputBuffer()
            .extract(index, index + maxLength);
        final int ret = trie.search(input);
        if (ret == -1)
            return false;

        context.advanceIndex(ret);
        return true;
    }

    /**
     * Accepts the given matcher visitor.
     *
     * @param visitor the visitor
     * @return the value returned by the given visitor
     */
    @Override
    public <R> R accept(final MatcherVisitor<R> visitor)
    {
        return null;
    }
}
